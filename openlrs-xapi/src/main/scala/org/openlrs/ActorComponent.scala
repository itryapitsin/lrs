package org.openlrs

import java.util.UUID

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
import org.openlrs.xapi.{Person, Agent, Actor, Activity}

/**
  * Created by iliyatryapitsin on 14/01/16.
  */
trait ActorComponent {
  this: BaseComponent with DocumentComponent =>

  case class ActorProfileDeleteParams1(agent:     Agent,
                                       profileId: ProfileId)

  case class ActorProfileDeleteParams2(agent:        Agent,
                                       activityId:   Activity#Id,
                                       stateId:      StateId,
                                       registration: Option[UUID])

  case class ActorProfileDeleteParams3(agent:        Agent,
                                       activityId:   Activity#Id,
                                       registration: Option[UUID])

  implicit val hasAgentInvoker: HasInvoker[Agent]
  implicit val selectActorProfileInvoker:  SelectByParamInvoker[Agent, Agent]
  implicit val deleteActorProfileInvoker1: DeleteInvoker[ActorProfileDeleteParams1]
  implicit val deleteActorProfileInvoker2: DeleteInvoker[ActorProfileDeleteParams2]
  implicit val deleteActorProfileInvoker3: DeleteInvoker[ActorProfileDeleteParams3]

  class ActorProfileStorage extends Rep[(Agent, ProfileId)] {
    def delete(agent: Agent, profileId: ProfileId)(implicit inv: DeleteInvoker[ActorProfileDeleteParams1]) =
      inv invoke ActorProfileDeleteParams1(agent, profileId)

    def delete(agent:        Agent,
               activityId:   Activity#Id,
               stateId:      StateId,
               registration: Option[UUID])(implicit inv: DeleteInvoker[ActorProfileDeleteParams2]) =
      inv invoke ActorProfileDeleteParams2(agent, activityId, stateId, registration)

    def delete(agent:        Agent,
               activityId:   Activity#Id,
               registration: Option[UUID])(implicit inv: DeleteInvoker[ActorProfileDeleteParams3]) =
      inv invoke ActorProfileDeleteParams3(agent, activityId, registration)
  }

  class ActorStorage extends Rep[Actor] {
    def has(agent: Agent)(implicit inv: HasInvoker[Agent]) = inv invoke agent

    def selectBy(agent: Agent)(implicit inv: SelectByParamInvoker[Agent, Agent]) =
      inv invoke agent
  }

  val actorStorage        = new ActorStorage
  val actorProfileStorage = new ActorProfileStorage

  /**
    * Delete the specified profile document in the context of the specified [[Agent]].
    * @param agent The [[Agent]] associated with this profile.
    * @param profileId The profile id associated with this profile.
    */
  def deleteProfile (agent:     Agent,
                     profileId: ProfileId): Unit = {

    documentStorage     delete (agent, profileId)
    actorProfileStorage delete (agent, profileId)

  }

  def deleteProfile(agent:        Agent,
                    activityId:   Activity#Id,
                    stateId:      String,
                    registration: Option[UUID]): Unit = {

    documentStorage     delete (agent, activityId, stateId, registration)
    actorProfileStorage delete (agent, activityId, stateId, registration)

  }

  def deleteProfiles(agent: Agent,
                     activityId: String,
                     registration: Option[UUID]): Unit = {

    documentStorage     delete (agent, activityId, registration)
    actorProfileStorage delete (agent, activityId, registration)
  }


  /**
    * Loads ids of all profile entries for an Agent. If "since" parameter is specified,
    * this is limited to entries that have been stored or updated since the specified timestamp (exclusive).
    * @param agent The Agent associated with this profile.
    * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
    * @return Lis of ids
    */
  def getProfiles(agent: Agent,
                  since: Option[DateTime] = None): Seq[ProfileId] =
    actorProfileStorage selectBy (agent, since)


  /**
    * Return a special, Person Object for a specified Agent.
    * The Person Object is very similar to an Agent Object, but instead of each attribute having a single value,
    * each attribute has an array value, and it is legal to include multiple identifying properties.
    * Note that the parameter is still a normal Agent Object with a single identifier and no arrays.
    * Note that this is different from the FOAF concept of person, person is being used here to indicate
    * a person-centric view of the LRS Agent data, but Agents just refer to one persona (a person in one context).
    * @param agent The Agent associated with this profile.
    * @return Person Object
    */
  def getPerson(agent: Agent): Person = {
    val actors = actorStorage selectBy agent

    Person(
      actors map { _.name        } takeDefined,
      actors map { _.mBox        } takeDefined,
      actors map { _.openId      } takeDefined,
      actors map { _.mBoxSha1Sum } takeDefined,
      actors map { _.account     } takeDefined
    )

  }
}

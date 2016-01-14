package com.arcusys.valamis.lrs.jdbc.database.api

import java.util.UUID

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan.{Document, Agent, Activity}

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait AgentProfileApi extends AgentProfileQueries {
  this: LrsDataContext
    with DocumentApi
    with ActorApi =>

  import driver.simple._

  /**
   * Delete Tincan [[com.arcusys.valamis.lrs.tincan.AgentProfile]] from a storage
   * @param actorKey Actor storage key
   * @param profileId Tincan ProfileId
   * @param session
   * @return Result code is a success if it great zero
   */
  def deleteAgentProfile (actorKey: ActorRow#Type, profileId: String)
                         (implicit session: Session) = {
    deleteDocument (actorKey, profileId)
    findAgentProfilesByActorAndProfileIdQC (actorKey, profileId) delete
  }

  /**
    * Get the specified profile document in the context of the specified Agent.
    * @param agent The Agent associated with this profile.
    * @param profileId The profile id associated with this profile.
    */
//  def getProfileContent(agent:     Agent,
//                        profileId: String): Option[Document] = db.withSession { implicit session =>
//    actors keyFor agent match {
//      case Some(key) => ??? //findAgentProfilesByActorAndProfileIdQC((key, profileId)).run map { x => x. }
//      case None => None
//    }
//  }

  def deleteAgentProfile (actorKey: ActorRow#Type,
                          activityId: Activity#Id,
                          stateId: String,
                          registration: Option[UUID])
                         (implicit session: Session) = {
    val documentKeys = findDocumentKeys (actorKey, activityId, stateId, registration)

    documents     filter { x => x.key         inSet documentKeys } delete

    agentProfiles filter { x => x.documentKey inSet documentKeys } delete
  }

}

package com.arcusys.valamis.lrs.jdbc

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.schema.AgentProfileSchema
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs._
import org.joda.time.DateTime

@deprecated
trait AgentComponent {
  this: LrsDataContext
    with AgentProfileSchema
    with StatementApi
    with DocumentApi
    with ActorApi
    with StatementObjectApi  =>

  import driver.simple._
  import jodaSupport._

//  /**
//   * Return a special, Person Object for a specified Agent.
//   * The Person Object is very similar to an Agent Object, but instead of each attribute having a single value,
//   * each attribute has an array value, and it is legal to include multiple identifying properties.
//   * Note that the parameter is still a normal Agent Object with a single identifier and no arrays.
//   * Note that this is different from the FOAF concept of person, person is being used here to indicate
//   * a person-centric view of the LRS Agent data, but Agents just refer to one persona (a person in one context).
//   * @param agent The Agent associated with this profile.
//   * @return Person Object
//   */
//  def getPerson(agent: Agent): Person = db withSession { implicit session =>
//
//    val filteredActors = actors leftJoin accounts on {
//      (actor, account) => actor.accountKey === account.key
//
//    } filter { case (actor, account) =>
//      val expr =
//        actor.mBox        === agent.mBox        ||
//        actor.mBoxSha1Sum === agent.mBoxSha1Sum ||
//        actor.openId      === agent.openId
//
//      agent.account map { a =>
//        account.name     === a.name     &&
//        account.homepage === a.homePage ||
//        expr
//      } getOrElse expr
//
//    } run
//
//    Person(
//      filteredActors map { case (a, _) => a.name        } takeDefined,
//      filteredActors map { case (a, _) => a.mBox        } takeDefined,
//      filteredActors map { case (a, _) => a.openId      } takeDefined,
//      filteredActors map { case (a, _) => a.mBoxSha1Sum } takeDefined,
//      filteredActors map { case (_, a) => Account(a.homepage, a.name) }
//    )
//  }

  /**
   * Loads ids of all profile entries for an Agent. If "since" parameter is specified,
   * this is limited to entries that have been stored or updated since the specified timestamp (exclusive).
   * @param agent The Agent associated with this profile.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return Lis of ids
   */
  def getProfiles(agent: Agent,
                  since: Option[DateTime] = None): Seq[String] = db.withSession { implicit session =>
    actors keyFor agent match {
      case Some(value) => getAgentDocuments(value, since) map { x => x.key }
      case None        => Seq()
    }
  }

  /**
   * Get the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def getProfileContent(agent:     Agent,
                        profileId: String): Option[Document] = db.withSession { implicit session =>
    actors keyFor agent match {
      case Some(key) => getAgentDocumentRow(key, profileId) map { x => x.toModel }
      case None => None
    }
  }

  /**
   * Store the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   * @param doc The document of profile
   */
  def addOrUpdateDocument(agent:     Agent,
                          profileId: String,
                          doc:       Document): Unit = db.withSession { implicit session =>
    val agentKey = statementObjects addExt agent

    getAgentDocumentRow(agentKey, profileId) match {
      case None =>
        documents     += DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType)
        agentProfiles += AgentProfileRow(profileId, agentKey, doc.id.toString)

      case Some(document) =>
        val newContent = if (doc.cType == ContentType.Json && document.cType == ContentType.Json)
          JsonHelper.combine(document.contents, doc.contents)
        else
          doc.contents

        val newDoc = document.copy(
          contents = newContent,
          cType    = doc.cType,
          updated  = DateTime.now
        )

        documents filter { x => x.key === newDoc.key } update newDoc
    }
  }

  private def agentProfileQuery(implicit session: Session) =
    agentProfiles
      .join(documents).on((sp, document) => sp.documentKey === document.key)

  private def filterAgentProfileQuery(agentKey:  AgentRow#Type,
                                      profileId: Option[String]   = None,
                                      since:     Option[DateTime] = None)
                                     (implicit session: Session) = {

    var query = agentProfileQuery filter { j => j._1.agentKey === agentKey }

    query = profileId match {
      case Some(value) => query filter { j => j._1.profileId === value }
      case None => query
    }

    query = since match {
      case Some(value) => query filter { j => j._2.updated >= value }
      case None => query
    }

    query
  }

  private def getAgentDocumentQuery(agentKey:  AgentRow#Type,
                                    profileId: Option[String]   = None,
                                    since:     Option[DateTime] = None)
                                   (implicit session: Session) = filterAgentProfileQuery(agentKey, profileId, since).map(x => x._2)

  private def getAgentDocumentRow(agentKey:  AgentRow#Type,
                                  profileId: String)
                                 (implicit session: Session) = getAgentDocumentQuery(agentKey, Option(profileId)).firstOption

  private def getAgentDocuments(agentKey: AgentRow#Type,
                                since:    Option[DateTime] = None)
                               (implicit session: Session) =
    getAgentDocumentQuery(agentKey, None, since).list.distinct

}

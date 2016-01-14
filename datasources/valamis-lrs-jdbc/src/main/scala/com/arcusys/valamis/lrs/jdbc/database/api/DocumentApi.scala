package com.arcusys.valamis.lrs.jdbc.database.api

import java.util.UUID

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.DocumentQueries
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait DocumentApi extends DocumentQueries
  with StatementObjectApi
  with AccountApi {
  this: LrsDataContext
    with SubStatementApi
    with ActivityApi
    with StatementRefApi
    with ActorApi=>

  import driver.simple._

  /**
    * Add or Update if exist the specified profile document in the context of the specified Activity.
    * @param activityId The activity id associated with these profiles.
    * @param profileId The profile id associated with this profile.
    * @param doc The new document version
    */
  def addOrUpdateDocument(activityId: String,
                          profileId: String,
                          doc: Document): Unit = db withSession { implicit session =>

    findDocument (activityId, profileId) match {
      case Some(rec) => updateDocument (doc, rec)
      case _         => addNewDocument (activityId, profileId, doc)
    }
  }

  def addOrUpdateDocument(agent: Agent,
                          activityId: String,
                          stateId: String,
                          registration: Option[UUID],
                          doc: Document): Unit = db withSession { implicit session =>
    val agentKey = actors addUnique agent

    findDocument(agentKey, activityId, stateId, registration) match {
      case Some(rec) => updateDocument (doc, rec)
      case _         => addNewDocument (agentKey, activityId, stateId, registration, doc)
    }
  }

  protected def addNewDocument(agentKey: AgentRow#Type,
                               activityId: String,
                               stateId: String,
                               registration: Option[UUID],
                               doc: Document)
                              (implicit session: Session) = {
    val activityKey = statementObjects addExt Activity(id = activityId)

    documents += DocumentRow(
      key      = doc.id.toString,
      cType    = doc.cType,
      contents = doc.contents
    )

    stateProfiles += StateProfileRow(
      stateId,
      agentKey,
      activityKey,
      registration map { _.toString },
      doc.id.toString
    )
  }

  protected def addNewDocument(activityId: String,
                               profileId: String,
                               doc: Document)
                              (implicit session: Session) = {
    val activityKey = statementObjects addExt Activity(id = activityId)

    documents += DocumentRow(
      key      = doc.id.toString,
      cType    = doc.cType,
      contents = doc.contents
    )

    activityProfiles += ActivityProfileRow(
      profileId   = profileId,
      activityKey = activityKey,
      documentKey = doc.id.toString
    )
  }

  protected def updateDocument(newDoc: Document,
                               oldDoc: Document)
                              (implicit session: Session) = {
    val newContent = if (newDoc.cType == ContentType.Json && oldDoc.cType == ContentType.Json)
      JsonHelper.combine(oldDoc.contents, newDoc.contents)
    else
      newDoc.contents

    val doc = DocumentRow(
      key      = oldDoc.id.toString,
      contents = newContent,
      cType    = newDoc.cType,
      updated  = DateTime.now)

    documents filter { x => x.key === doc.key } update doc
  }

  /**
   * Delete Tincan [[com.arcusys.valamis.lrs.tincan.Document]] from a storage
   * @param actorKey Actor storage key
   * @param profileId Tincan ProfileId
   * @param session
   * @return Result code is a success if it great zero
   */
  def deleteDocument (actorKey: ActorRow#Type, profileId: ProfileId)
                     (implicit session: Session) =
    findDocumentsByActorAndProfileIdQC (actorKey, profileId) delete

  def findDocument (activityId: Activity#Id,
                    profileId:  ProfileId)
                   (implicit session: Session): Option[Document] =
    findDocumentsByActivityIdAndProfileIdQC (activityId, profileId).firstOption map {
      x => x convert
    }

  def findDocumentKeys (actorKey:     ActorRow#Type,
                        activityId:   Activity#Id,
                        registration: Option[UUID],
                        since:        Option[DateTime])
                       (implicit session: Session): Seq[String] = {
    val keys = for {
      reg <- registration
      s   <- since
    } yield findDocumentKeysByActorAndActivityIdAndRegistrationAndSinceQC (
      (actorKey, activityId, reg toString, s)
    ).run

    keys getOrElse {
      findDocumentKeysByActorAndActivityIdQC ((actorKey, activityId)) run
    }
  }

  def findDocument (actorKey:     ActorRow#Type,
                    activityId:   Activity#Id,
                    stateId:      String,
                    registration: Option[UUID])
                   (implicit session: Session): Option[Document] = registration map { reg =>
    findDocumentsByActorAndActivityIdAndStateIdAndRegistrationQC((actorKey, activityId, stateId, reg toString)) firstOption
  } getOrElse {
    findDocumentsByActorAndActivityIdAndStateIdQC (actorKey, activityId, stateId) firstOption
  } map { x => x convert }

  def findDocumentKeys (actorKey: ActorRow#Type,
                        activityId: Activity#Id,
                        stateId: String,
                        registration: Option[UUID])
                       (implicit session: Session) = registration map { reg =>
    findDocumentKeysByActorAndActivityIdAndStateIdAndRegistrationQC((actorKey, activityId, stateId, reg toString)) run
  } getOrElse {
    findDocumentKeysByActorAndActivityIdAndStateIdQC (actorKey, activityId, stateId) run
  }
}

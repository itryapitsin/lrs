package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.ActivityProfileQueries

/**
 * Created by Iliya Tryapitsin on 13.07.15.
 */
trait ActivityProfileApi extends ActivityProfileQueries {
  this: LrsDataContext =>

  //  def getProfile(agent: Agent,
  //                 activityId: String,
  //                 stateId: String,
  //                 registration: Option[UUID]): Option[Document] =
  //    db.withSession { implicit session =>
  //      actors keyFor agent match {
  //        case Some(value) => getDocumentRow(value, activityId, stateId, registration).map(x => x.toModel)
  //        case None => None
  //      }
  //    }

  //  def getProfiles(agent: Agent,
  //                  activityId: String,
  //                  registration: Option[UUID],
  //                  since: Option[DateTime]): Seq[String] = db.withSession { implicit session =>
  //    actors keyFor agent match {
  //      case Some(value) => getDocuments(value, activityId, registration, since).map(x => x.key)
  //      case None => Seq()
  //    }
  //  }

  //  def deleteProfiles(agent: Agent,
  //                     activityId: String,
  //                     registration: Option[UUID]): Unit = db.withSession { implicit session =>
  //    actors keyFor agent match {
  //      case Some(value) =>
  //        val docKeys = getDocuments(value, activityId, registration, None) map { x => x.key }
  //        documents filter { x => x.key inSet docKeys } delete
  //
  //      case None => Unit
  //    }
  //  }
}

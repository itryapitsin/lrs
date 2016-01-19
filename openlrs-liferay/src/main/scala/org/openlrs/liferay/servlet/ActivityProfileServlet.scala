package org.openlrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.openlrs.liferay.servlet
import com.arcusys.valamis.lrs.liferay.exception._
import org.openlrs.liferay.servlet.request.TincanActivityProfileRequest
import com.google.inject.{Inject, Injector, Singleton}
import org.openlrs.liferay.exception.{NotFoundException, InvalidOrMissingArgumentException}
import org.openlrs.xapi.ContentType

/**
 * Created by Iliya Tryapitsin on 25/12/14.
 */

@Singleton
class ActivityProfileServlet extends BaseLrsServlet {

  override def doGet(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanActivityProfileRequest](model => {

    model.profileId match {
      case None => lrs.getProfileIds(model.activityId, model.since)
      case Some(profileId) => lrs.getDocument(model.activityId, profileId) match {
        case None => throw new NotFoundException("Activity Profile")
        case Some(document) => {
          // TODO: Check response for Other content type
          if (document.cType == ContentType.Other) {
            response.setContentType("application/octet-stream")
            val data = document.contents.getBytes()
            response.getOutputStream.write(data)
            document.contents
          } else document.contents
        }
      }
    }
  }, request, response)

  override def doPost(request: HttpServletRequest,
                      response: HttpServletResponse): Unit = jsonAction[TincanActivityProfileRequest](model => {

    model.profileId match {
      case Some(profileId) => lrs.addOrUpdateDocument(model.activityId, profileId, model.document)
      case None => throw new InvalidOrMissingArgumentException("profileId")
    }
    Unit
  }, request, response)

  override def doPut(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanActivityProfileRequest](model => {

    model.profileId match {
      case Some(profileId) => lrs.addOrUpdateDocument(model.activityId, profileId, model.document)
      case None => throw new InvalidOrMissingArgumentException("profileId")
    }
    Unit
  }, request, response)

  override def doDelete(request: HttpServletRequest,
                        response: HttpServletResponse): Unit = jsonAction[TincanActivityProfileRequest](model => {

    model.profileId match {
      case Some(profileId) => lrs.deleteActivityProfile(model.activityId, profileId)
      case None => throw new InvalidOrMissingArgumentException("profileId")
    }
    Unit
  }, request, response)

  override val ServletName: String = "ActivityProfile"
}

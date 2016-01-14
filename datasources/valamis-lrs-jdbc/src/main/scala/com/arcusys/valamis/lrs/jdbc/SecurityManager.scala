package com.arcusys.valamis.lrs.jdbc

import java.util.UUID

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.SecurityDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.{ApplicationRow, TokenRow}
import com.arcusys.valamis.lrs.security.{AuthenticationStatus, AuthenticationType}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait SecurityManager extends SecurityDataContext with Loggable {

  import driver.simple._
  import jodaSupport._

  val expiredPeriod = 60 * 60 * 24

  // One day
  def expiredDateTime = DateTime.now.minusSeconds(expiredPeriod)

  private def getAppScope(appId: String) = {
    val query = applications filter {
      x => x.appId === appId
    } map {
      app => app.scope
    }

    db withSession { implicit s =>
      query.firstOption
    }
  }

  def getCallback(token: String) = {
    val query = tokens filter { x => x.code === token } map { x => x.callback }

    db withSession { implicit s =>
      query.firstOption
    }
  }

  def getApplications(count: Int, offset: Int) =  {
    val query = applications take count drop offset

    db withSession { implicit s =>
      query.run
    }
  }

  def checkByToken(appId: String,
                   token: String,
                   scopeRequest: AuthorizationScope.ValueSet) =
    getAppScope(appId) match {
      case None        => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val query = tokens filter { t =>
          (t.appId === appId) &&
          (t.token === token) &&
          (t.issueAt > expiredDateTime)
        }

        val isExistValidToken = db withSession { implicit s => query.firstOption.isDefined }

        if (accessRightSuccess) AuthenticationStatus(isExistValidToken)
        else AuthenticationStatus.Forbidden
    }

  def checkByBasic(clientId:     String,
                   secret:       String,
                   scopeRequest: AuthorizationScope.ValueSet) =
    getAppScope(clientId) match {
      case None        => AuthenticationStatus.Denied

      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val query = applications filter { x =>
          (x.appId === clientId) && (x.appSecret === secret)
        }

        val valid = db withSession { implicit s => query.firstOption.isDefined }

        if (accessRightSuccess) AuthenticationStatus(valid)
        else AuthenticationStatus.Forbidden
    }

  def setAuthorized(appId:       String,
                    requestCode: String,
                    verified:    String) = {
      val query = tokens filter { t =>
        (t.appId === appId) && (t.code === requestCode)

      } map { t => t.verifier }

      db withSession { implicit s =>
        query update verified.toOption match {
          case e if e >= 0 =>
            logger.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode")

          case e if e < 0 =>
            logger.error(s"Set access token failure: ${query.updateStatement} with User Id = None, App Id = $appId, Request Code = $requestCode")
        }
      }
    }

  def setAccessToken(appId:        String,
                     requestCode:  String,
                     accessCode:   String,
                     accessSecret: String) = {

      val query = tokens filter {
        t => (t.appId === appId) && (t.code === requestCode)
      } map { t => (t.issueAt, t.token, t.tokenSecret) }

      db withSession { implicit s =>
        query update (DateTime.now, accessCode.toOption, accessSecret.toOption) match {
          case e if e >= 0 =>
            logger.debug(s"Set access token success: User Id = None, App Id = $appId, Access Code = $accessCode")

          case e if e < 0 =>
            logger.error(s"Set access token failure: ${tokens.updateStatement} with User Id = None, App Id = $appId, Access Code = $accessCode")
        }
      }
    }

  def setRequestToken(appId:         String,
                      requestCode:   String,
                      requestSecret: String,
                      callback:      String) = {
    val token = TokenRow(None, appId, requestCode, requestSecret, callback, DateTime.now)

    db withSession { implicit s =>
      tokens += token match {
        case e if e >= 0 =>
          logger.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode, callback = $callback")

        case e if e < 0 =>
          logger.error(s"Set access token failure: ${tokens.insertStatement} with User Id = None, App Id = $appId, Access Code = $requestCode")
      }
    }
  }

  def getRequestToken(appId: String,
                      requestCode: String) = {
    val query = tokens filter { t =>
      t.token.isNull &&
        t.code === requestCode &&
        t.issueAt > expiredDateTime
    }

    db withSession { implicit s =>
      query.firstOption
    }
  }

  def getAccessToken(appId: String,
                     token: String) = {
    val query = tokens filter { t =>
      t.appId === appId &&
      t.token === token &&
      t.issueAt > expiredDateTime
    }

    db withSession { implicit s =>
      query.firstOption
    }
  }

  def registrationApp(appName:        String,
                      appDescription: Option[String],
                      scope:          AuthorizationScope.ValueSet,
                      authType:       AuthenticationType.Type) = {
    val app = ApplicationRow(
      appId = UUID.randomUUID.toString,
      name = appName,
      description = appDescription.orNull,
      appSecret = UUID.randomUUID.toString,
      scope = scope,
      regDateTime = DateTime.now,
      authType = authType
    )

    db withSession { implicit s =>
      applications += app match {
        case e if e >= 0 =>
          logger.debug(s"Registered application success: App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
          app ?

        case e if e < 0 =>
          logger.error(s"Registered application failure: ${applications.insertStatement} with App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
          None
      }
    }
  }

  def getApplication(appId: String) = {
    val query = applications filter { x => x.appId === appId }

    db withSession { implicit s =>
      query.firstOption
    }
  }

  def updateApplication(appId:    String,
                        name:     String,
                        desc:     Option[String],
                        scope:    AuthorizationScope.ValueSet,
                        authType: AuthenticationType.Type) = {
    val query = applications filter { x =>
      x.appId === appId

    } map { x =>
      (x.name, x.scope, x.description, x.authType)
    }

    db withSession { implicit s =>
      query update (name, scope, desc.orNull, authType) match {
        case e if e >= 0 =>
          logger.debug(s"Update application success: App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")

        case e if e < 0 =>
          logger.error(s"Update application failure with App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")
      }
    }

  }

  def deleteApplication(appId: String) = {
    val query = applications filter { x => x.appId === appId }

    db withSession { implicit session =>
      query.delete match {
        case e if e >= 0 =>
          logger.debug(s"Delete application success: App Id = ${appId}")

        case e if e < 0 =>
          logger.error(s"Delete application failure ${query.deleteStatement} with App Id = ${appId}")
      }
    }
  }

  def blockApplication(appId: String) = {

    val query = applications filter {
      app => app.appId === appId
    } map {
      app => app.isActive
    }

    db withSession { implicit s =>
      query update false match {
        case e if e >= 0 =>
          logger.debug(s"Block application success: App Id = ${appId}")

        case e if e < 0 =>
          logger.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
      }
    }
  }

  def unblockApplication(appId: String) = {

    val query = applications filter {
      app => app.appId === appId
    } map {
      app => app.isActive
    }

    db withSession { implicit s =>
      query update true match {
        case e if e >= 0 =>
          logger.debug(s"Block application success: App Id = ${appId}")

        case e if e < 0 =>
          logger.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
      }
    }
  }

  def clear() = {
    val query = tokens filter { t => t.issueAt < expiredDateTime}

    db withSession { implicit s =>
      query.delete match {
        case e if e >= 0 =>
          logger.debug(s"Expired tokens remove success: $e tokens was removed")

        case e if e < 0 =>
          logger.error(s"Expired tokens remove failure: ${query.deleteStatement}")
      }
    }
  }
}

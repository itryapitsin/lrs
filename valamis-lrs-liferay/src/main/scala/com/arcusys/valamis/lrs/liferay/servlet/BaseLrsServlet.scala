package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.ServletConfig
import javax.servlet.http._

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.{LrsType, Instrumented}
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.message.Broker
import com.arcusys.valamis.lrs.tincan.Constants.Headers
import com.arcusys.valamis.lrs.tincan.TincanVersion
import com.codahale.metrics.servlets.MetricsServlet
import com.codahale.metrics.{MetricRegistry, Timer}
import com.google.inject.name.Names
import com.google.inject.{Key, Injector}
import com.liferay.portal.model.User
import com.liferay.portal.security.auth.{CompanyThreadLocal, PrincipalThreadLocal}
import com.liferay.portal.security.permission.{PermissionCheckerFactoryUtil, PermissionThreadLocal}
import com.liferay.portal.service.UserLocalServiceUtil
import com.liferay.portal.util.PortalUtil
import com.arcusys.valamis.lrs.protocol._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

abstract class BaseLrsServlet
  extends BaseServlet
  with JsonServlet
  with LrsTypeLocator
  with Instrumented {

  protected lazy val lrs             = Lrs
  protected lazy val reporter        = ValamisReporter
  protected lazy val securityManager = SecurityManager

  val ServletName: String

  protected var requestsTimer: Timer = _
  protected var registry: MetricRegistry = _

  override def setHeaders(response: HttpServletResponse): Unit = {
    response.addHeader(XExperienceAPIConsistentThrough, new DateTime().toString(ISODateTimeFormat.dateTime()))
    response.addHeader(Headers.Version, TincanVersion.ver101.toString)
  }

  override def init(config: ServletConfig): Unit = {
    getRegistry(config)
    requestsTimer = registry timer ServletName
  }

  def getRegistry(config: ServletConfig) {
    val context = config.getServletContext()
    this.registry = context
      .getAttribute(MetricsServlet.METRICS_REGISTRY)
      .asInstanceOf[MetricRegistry]
  }

  protected def noContent = throw new NoSuchElementException

  def getUserByRequest(request: HttpServletRequest): User = {

    val user = PortalUtil.getUser(request) match {
      case u: User if u == null  =>
        UserLocalServiceUtil.getDefaultUser(PortalUtil.getDefaultCompanyId)

      case u: User => u
    }

    val permissionChecker = PermissionCheckerFactoryUtil.create(user)

    PermissionThreadLocal.setPermissionChecker(permissionChecker)
    PrincipalThreadLocal.setName(user.getUserId)
    CompanyThreadLocal.setCompanyId(user.getCompanyId)
    user
  }
}

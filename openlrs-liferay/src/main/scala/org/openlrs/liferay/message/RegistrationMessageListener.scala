package org.openlrs.liferay.message

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.liferay._
import org.openlrs.liferay.util.LiferayDbContext
import com.arcusys.json.JsonHelper
import com.liferay.portal.kernel.messaging.{Message, MessageBusUtil, MessageListener}
import org.openlrs.liferay.history.SQLRunner
import org.openlrs.liferay.{SecurityManager, Loggable}
import org.openlrs.security.AuthenticationType
import org.openlrs.xapi.AuthorizationScope

class RegistrationMessageListener extends MessageListener with Loggable with SQLRunner {

  override def receive(message: Message): Unit = {

    LiferayDbContext.init()

    logger.info("'Register new app' message received")

    val appName        = message.get("appName"       ).asInstanceOf[String]
    val appDescription = message.get("appDescription").asInstanceOf[String].toOption
    val authScope      = AuthorizationScope.fromString(message.get("authScope").asInstanceOf[String])
    val authType       = AuthenticationType.withName  (message.get("authType" ).asInstanceOf[String])

    val application = SecurityManager.registrationApp(appName, appDescription, authScope, authType)
    val endpoint    = liferayUrlPrefix
    val response    = MessageBusUtil.createResponseMessage(message)

    response.setPayload(application match {
      case Some(a) => JsonHelper.toJson(new ResponseModel(a.appId, a.appSecret, endpoint))
      case None    => "None"  //payload must exists in any case
    })

    MessageBusUtil.sendMessage(response.getDestinationName, response)
  }

  case class ResponseModel(appId: String, appSecret: String, endpoint: String)

}

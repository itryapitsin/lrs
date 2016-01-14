package com.arcusys.valamis.lrs.liferay.message

import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.arcusys.valamis.lrs.liferay.LrsTypeLocator
import com.arcusys.valamis.lrs.liferay.util.LiferayDbContext
import com.liferay.portal.kernel.events.SimpleAction
import com.liferay.portal.kernel.messaging.{Message, MessageBusUtil}

/**
  * Created by Iliya Tryapitsin on 23.06.15.
  */
class LrsDeployedAction extends SimpleAction with SQLRunner with LrsTypeLocator {

  override def run(companyIds: Array[String]): Unit = {
    LiferayDbContext.setScope(companyIds.head.toLong)

    logger.info("Send startup message")
    val message = new Message()
    message.put("lrs", "deployed")

    MessageBusUtil.sendMessage("valamis/main/lrsDeployed", message)
  }
}

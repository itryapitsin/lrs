package org.openlrs.liferay.message

import com.arcusys.valamis.lrs.liferay._
import com.google.inject._
import com.google.inject.name.Names
import com.liferay.portal.kernel.messaging.{Message, MessageListener}
import org.openlrs.LrsType

/**
 * Created by Iliya Tryapitsin on 22.05.15.
 */

@Singleton
class TokenCleanScheduler extends MessageListener with LrsTypeLocator {

  override def receive(message: Message): Unit = {
//    val injector       = Guice.createInjector(new LrsModule)
//    val lrsType        = LrsType.Simple
    val authentication = sm //injector.getInstance(Key.get(classOf[SecurityManager], Names.named(lrsType.toString)))

    logger.info("Start cleaning")

    authentication.clear()
  }
}

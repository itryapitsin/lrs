package org.openlrs.liferay.history

/**
 * Created by Iliya Tryapitsin on 24.04.15.
 */

import org.openlrs.liferay.history.ver230.{DbUpgradeProcess => Ver230}
import org.openlrs.liferay.history.ver240.{DbUpgradeProcess => Ver240}
import org.openlrs.liferay.history.ver250.{DbUpgradeProcess => Ver250}
import org.openlrs.liferay.util.LiferayDbContext
import com.liferay.portal.kernel.events.SimpleAction

import scala.util.{Failure, Success, Try}

class SetupSchema extends SimpleAction with SQLRunner {

  override def run(companyIds: Array[String]): Unit = Try {
    LiferayDbContext.setScope(companyIds.head.toLong)

    val ver230 = new Ver230
    ver230.doUpgrade()

    val ver240 = new Ver240
    ver240.doUpgrade()

    val ver250 = new Ver250
    ver250.doUpgrade()
  } match {
    case Success(_)  => logger.info("Db schema has been upgraded successfully.")
    case Failure(ex) => logger.error(ex)
  }
}
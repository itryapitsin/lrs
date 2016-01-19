package org.openlrs.liferay.history.ver230.from

import java.util.UUID

  import org.openlrs.liferay.history.ver230.from.row.ActivityStateRow
import org.openlrs.liferay.history.BaseComponent

import scala.slick.jdbc._

/**
 * Created by Iliya Tryapitsin on 26/03/15.
 */
trait ActivityStateComponent { this: BaseComponent =>
  private implicit val getResult = GetResult(r => {
    ActivityStateRow(
      r.nextLong(),         //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`stateId` varchar(512) DEFAULT NULL,
      r.nextStringOption().map { s => UUID.fromString(s) }, //`documentId` varchar(512) DEFAULT NULL,
      r.nextStringOption(), //`activityId` varchar(512) DEFAULT NULL,
      r.nextStringOption(),       //`registration` longtext,
      r.nextLongOption()    //`agentId` int(11) DEFAULT NULL,
    )
  })

  protected def activityStates(implicit session: JdbcBackend#Session) = loadActivityStates

  def loadActivityStates(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[ActivityStateRow](s"select * from ${getTableName("Learn_LFTincanLrsState")}")
    .list
}
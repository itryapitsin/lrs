package org.openlrs.liferay.history.ver230.from

  import org.openlrs.liferay.history.ver230.from.row.AgentProfileRow
import org.openlrs.liferay.history.{BaseComponent, Helper}

import scala.slick.jdbc.{GetResult, JdbcBackend, StaticQuery}
import Helper._

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait AgentProfileComponent {
  this: BaseComponent =>

  private implicit val getResult = GetResult(r => {
    AgentProfileRow(
      r.nextLong(),         //`id_` bigint(20) NOT NULL,
      getUUIDOption(r.nextStringOption()), //`documentId` int(11) DEFAULT NULL,
      r.nextLongOption(),   //`agentId` int(11) DEFAULT NULL,
      r.nextStringOption()  //`profileId` varchar(75) DEFAULT NULL,
    )
  })

  protected def agentProfiles(implicit session: JdbcBackend#Session) = loadAgentProfiles

  def loadAgentProfiles(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[AgentProfileRow](s"select * from ${getTableName("Learn_LFTCLrsAgentProfile")}")
    .list
}
package org.openlrs.liferay.servlet.request.valamis

import java.net.URI
import javax.servlet.http.HttpServletRequest

import com.arcusys.json.JsonHelper
import org.openlrs.liferay.servlet.request.{BaseLrsRequest, TincanStatementRequest}
import org.joda.time.DateTime
import org.openlrs.serializer.AgentSerializer
import org.openlrs.xapi.Agent

/**
 * Created by Iliya Tryapitsin on 13.08.15.
 */
class ValamisStatementsRequest (r: HttpServletRequest)
  extends BaseLrsRequest(r)
  with ValamisActionRequestComponent {

  val Agent = "agent"
  val Verbs = "verbs"
  val Since = "since"
  val ActivityIds = "activity-ids"

  val FindMinDate = "find-min-date"
  val GetCountByParams = "get-count-by-params"

  def agent = JsonHelper.fromJson[Agent](
    require(Agent), new AgentSerializer
  )

  def verbs = JsonHelper.fromJson[Seq[String]](
    require(Verbs)
  ) map { new URI(_) }

  def activityIds = JsonHelper.fromJson[Seq[String]](
    require(ActivityIds)
  ) map { new URI(_) }

  def since = new DateTime(
    require(Since)
  )

}

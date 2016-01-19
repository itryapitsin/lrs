package org.openlrs.liferay.servlet.request

import org.openlrs.liferay.servlet.request.BaseTincanAgentRequestComponent.Agent
import com.arcusys.json.JsonHelper
import BaseTincanAgentRequestComponent.{ Agent => a }
import org.openlrs.serializer.AgentSerializer
import org.openlrs.xapi.Agent

/**
  * Created by Iliya Tryapitsin on 29/12/14.
 */

trait BaseTincanAgentRequestComponent {
  r: BaseLrsRequest =>

  def agent(implicit m: Manifest[Agent]) = JsonHelper.fromJson[Agent](require(a), new AgentSerializer)
}

object BaseTincanAgentRequestComponent {
  val Agent = "agent"
}
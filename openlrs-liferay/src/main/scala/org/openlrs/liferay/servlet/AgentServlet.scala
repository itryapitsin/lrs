package org.openlrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.openlrs.liferay.servlet
import org.openlrs.liferay.servlet.request.TincanAgentProfileRequest
import com.google.inject.{Inject, Injector, Singleton}

/**
 * Created by iliyatryapitsin on 25/12/14.
 */

@Singleton
class AgentServlet extends BaseLrsServlet {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](

    model => lrs.getPerson(model.agent), request, response)

  override val ServletName: String = "Agent"
}
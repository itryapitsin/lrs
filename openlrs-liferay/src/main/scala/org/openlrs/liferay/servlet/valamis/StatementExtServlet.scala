package org.openlrs.liferay.servlet.valamis

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.openlrs.liferay.servlet.BaseLrsServlet
import org.openlrs.liferay.servlet.request.valamis.ValamisStatementsRequest
import com.google.inject._

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
@Singleton
class StatementExtServlet extends BaseLrsServlet {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[ValamisStatementsRequest]({ model =>

    model.action match {
      case model.GetCountByParams =>
        reporter.findStatementsCount(model.agent, model.verbs)

      case model.FindMinDate      =>
        reporter.findMinDate(model.agent, model.verbs, model.activityIds, model.since)

    }
  }, request, response)

  override val ServletName: String = "StatementExt"
}
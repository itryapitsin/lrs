package org.openlrs.liferay.servlet.valamis

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.openlrs.liferay.servlet.BaseLrsServlet
import org.openlrs.liferay.servlet.request.valamis.ValamisVerbRequest
import com.google.inject._
import org.openlrs.SeqWithCount
import org.openlrs.xapi.valamis.VerbStatistics

/**
 * Created by Iliya Tryapitsin on 15.06.15.
 */

@Singleton
class VerbServlet extends BaseLrsServlet {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[ValamisVerbRequest]({ model =>

    model.action match {
      case model.VerbStatistics =>
        VerbStatistics(
          amount = reporter verbAmount model.since,
          byGroup = reporter verbAmountByGroup model.since,
          withDatetime = reporter verbIdsWithDate model.since
        )

      case model.VerbsWithActivities =>
        reporter.verbWithActivities(model.filter,
          model.limit,
          model.offset,
          model.nameSort,
          model.timeSort,
          model.sortTimeFirst)

      case model.VerbsAmount =>
        reporter.verbAmount(model.since, model.verb)
    }
  }, request, response)

  override val ServletName: String = "Verb"
}

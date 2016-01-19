package org.openlrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.openlrs.liferay.servlet
import org.openlrs.liferay.servlet.request.TincanActivityRequest
import com.google.inject.{Inject, Injector, Singleton}
import org.openlrs.liferay.exception.NotFoundException

/**
 * Created by iliyatryapitsin on 25/12/14.
 */
@Singleton
class ActivityServlet extends BaseLrsServlet {

  /**
   * Loads the complete Activity Object specified.
   * @param request Required activityId
   * @param response Returns: 200 OK, Content
   */
  override def doGet(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanActivityRequest]({ model =>

    if(model.hasActivityName)
      lrs.getActivities(model.activityName)
    else
      lrs.getActivity(model.activityId) match {
        case Some(activity) => activity
        case None => throw new NotFoundException("Activity")
      }
  }, request, response)

  override val ServletName: String = "Activity"
}

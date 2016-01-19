package org.openlrs.liferay.portlet

import javax.portlet._

import com.arcusys.valamis.lrs._
import html.apps.html._
import org.openlrs.RunningMode
import org.openlrs.liferay.LrsModeLocator

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
class LrsModeView extends GenericPortlet with LrsModeLocator {

//  lazy val injector        = Guice.createInjector(new LrsModule)
  lazy val securityManager = sm //injector.getInstance(Key.get(classOf[SecurityManager], Names.named(LrsType.Simple.toString)))

  private def getAction(r: PortletRequest) = {
    val a = r.getParameter(Action.Name)
    if(a == null || a.isEmpty) None
    else (Action withName a) toOption
  }

  override def processAction(request: ActionRequest, response: ActionResponse): Unit = {
    val act  = getAction(request)

    act match {

      case Some(Action.LrsModeChanged) =>
        val mode = request getParameter "currentMode"
        saveLrsModeSettings(mode)

        // apply selected mode
        RunningMode setCurrent mode

      case _ =>
    }
  }

  override def doView(request: RenderRequest, response: RenderResponse) {

    val mode = RunningMode.current
    val clientApiListView = lrsMode(
      new LrsModeViewModel(mode)(request, response)
    )
    response.getWriter write (index(clientApiListView) toString)
  }
}
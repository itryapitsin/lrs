package org.openlrs.liferay.portlet

import javax.portlet.{RenderResponse, RenderRequest}
import com.arcusys.valamis.lrs.jdbc.database.row.ApplicationRow
import org.openlrs.security.AuthenticationType
import org.openlrs.xapi.AuthorizationScope
import AuthorizationScope._
import org.openlrs.LrsType
import org.openlrs.xapi.AuthorizationScope

/**
 * Created by Iliya Tryapitsin on 26.04.15.
 */
class AppListPortletViewModel(val apps:    Seq[ApplicationRow],
                              val lrsType: LrsType.Type)
                             (implicit request: RenderRequest,
                         response: RenderResponse) extends BasePortletView(request, response) {
  def isSelectedLrsType(tpe: LrsType.Type) = if (lrsType == tpe) "selected" else ""
}

class AppAddOrEditPortletViewModel(val selectedApp: Option[ApplicationRow] = None)
                                  (implicit request: RenderRequest,
                              response: RenderResponse) extends BasePortletView(request, response) {
  def action = selectedApp match {
    case None    => Action.Add
    case Some(_) => Action.Edit
  }

  def isSelectedScope(scope: AuthorizationScope.Type) = selectedApp match {
    case Some(v) => if(v.scope <== scope.toValueSet) "checked" else ""
    case None => ""
  }

  def isSelectedAuthType(authType: AuthenticationType.Type) = selectedApp match {
    case Some(v) => if(v.authType == authType) "selected" else ""
    case None => ""
  }
}
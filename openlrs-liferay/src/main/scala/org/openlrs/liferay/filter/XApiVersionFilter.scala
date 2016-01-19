package org.openlrs.liferay.filter

import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.google.inject.Singleton
import org.openlrs.xapi.{Constants, TincanVersion}

import scala.util._

/**
 * Created by Iliya Tryapitsin on 14/02/15.
 */

@Singleton
class XApiVersionFilter() extends Filter {

  override def doFilter(request: ServletRequest,
                        response: ServletResponse,
                        filterChain: FilterChain) = {

    val req = request.asInstanceOf[HttpServletRequest]
    val res = response.asInstanceOf[HttpServletResponse]
    if (req.getMethod.equalsIgnoreCase(Options)) filterChain.doFilter(request, response)
    else Try {
      val version = req.getHeader(Constants.Headers.Version)
      TincanVersion.withName(version)
    } match {
      case Success(_) => filterChain.doFilter(request, response)
      case Failure(_) => res.setStatus(HttpServletResponse.SC_BAD_REQUEST)
    }
  }

  override def init(filterConfig: FilterConfig) = Unit

  override def destroy() = Unit
}

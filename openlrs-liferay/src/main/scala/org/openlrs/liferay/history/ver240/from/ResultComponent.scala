package org.openlrs.liferay.history.ver240.from

import org.openlrs.liferay.history.BaseComponent

import scala.slick.jdbc._
import java.net.URLDecoder

trait ResultComponent {
  this: BaseComponent =>

  def encodeResults()(implicit session: JdbcBackend#Session) = StaticQuery.queryNA[(Int, String)](s"select key, response from lrs_results where response is not NULL")
      .foreach {c =>
        updateResults(s" update lrs_results set response='${URLDecoder.decode(c._2, "UTF-8")}' where key=${c._1}")
      }

  private def updateResults(query: String)(implicit session: JdbcBackend#Session) = StaticQuery
    .updateNA(query).execute(session)
}

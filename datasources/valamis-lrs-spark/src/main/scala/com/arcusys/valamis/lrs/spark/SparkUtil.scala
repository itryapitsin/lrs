package com.arcusys.valamis.lrs

import java.sql.Date

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 27.07.15.
 */
package object spark {
  implicit class JodaExtensions (val dt: DateTime) extends AnyVal {
    def toSql = new Date(dt.getMillis)
  }
}

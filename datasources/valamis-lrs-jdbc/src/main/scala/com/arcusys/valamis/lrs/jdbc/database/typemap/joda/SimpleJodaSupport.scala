package com.arcusys.valamis.lrs.jdbc.database.typemap.joda

import javax.inject.Inject

import scala.slick.driver.JdbcDriver

/**
 * Created by Iliya Tryapitsin on 03.08.15.
 */
class SimpleJodaSupport @Inject() (val driver: JdbcDriver) extends JodaSupport

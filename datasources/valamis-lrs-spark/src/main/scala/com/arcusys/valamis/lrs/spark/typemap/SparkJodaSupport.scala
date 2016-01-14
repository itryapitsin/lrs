package com.arcusys.valamis.lrs.spark.typemap

import javax.inject.Inject

import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.JodaSupport

import scala.slick.driver.JdbcDriver

/**
 * Created by Iliya Tryapitsin on 04.08.15.
 */
class SparkJodaSupport @Inject() (val driver: JdbcDriver) extends JodaSupport {
//  protected val localDateTimeMapperDelegate = new JodaLocalDateTimeMapper(driver)
//  protected val dateTimeZoneMapperDelegate  = new JodaDateTimeZoneMapper (driver)
//  protected val localDateMapperDelegate     = new JodaLocalDateMapper    (driver)
//  protected val localTimeMapperDelegate     = new JodaLocalTimeMapper    (driver)
//  protected val dateTimeMapperDelegate      = new JodaDateTimeMapper     (driver)
//  protected val instantMapperDelegate       = new JodaInstantMapper      (driver)
}

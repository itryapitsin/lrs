package com.arcusys.valamis.lrs.jdbc.database.typemap.joda

import scala.slick.driver.JdbcDriver

trait JodaSupport {
  val driver: JdbcDriver

  protected val localDateTimeMapperDelegate = new JodaLocalDateTimeMapper(driver)
  protected val dateTimeZoneMapperDelegate  = new JodaDateTimeZoneMapper (driver)
  protected val localDateMapperDelegate     = new JodaLocalDateMapper    (driver)
  protected val localTimeMapperDelegate     = new JodaLocalTimeMapper    (driver)
  protected val dateTimeMapperDelegate      = new JodaDateTimeMapper     (driver)
  protected val instantMapperDelegate       = new JodaInstantMapper      (driver)

  implicit lazy val dateTimeZoneTypeMapper         = dateTimeZoneMapperDelegate.typeMapper
  implicit lazy val getDateTimeZoneResult          = dateTimeZoneMapperDelegate.getResult.getResult
  implicit lazy val getDateTimeZoneOptionResult    = dateTimeZoneMapperDelegate.getResult.getOptionResult
  implicit lazy val setDateTimeZoneParameter       = dateTimeZoneMapperDelegate.setParameter.setParameter
  implicit lazy val setDateTimeZoneOptionParameter = dateTimeZoneMapperDelegate.setParameter.setOptionParameter

  implicit lazy val localDateTypeMapper         = localDateMapperDelegate.typeMapper
  implicit lazy val getLocalDateResult          = localDateMapperDelegate.getResult.getResult
  implicit lazy val getLocalDateOptionResult    = localDateMapperDelegate.getResult.getOptionResult
  implicit lazy val setLocalDateParameter       = localDateMapperDelegate.setParameter.setParameter
  implicit lazy val setLocalDateOptionParameter = localDateMapperDelegate.setParameter.setOptionParameter

  implicit lazy val datetimeTypeMapper         = dateTimeMapperDelegate.typeMapper
  implicit lazy val getDatetimeResult          = dateTimeMapperDelegate.getResult.getResult
  implicit lazy val getDatetimeOptionResult    = dateTimeMapperDelegate.getResult.getOptionResult
  implicit lazy val setDatetimeParameter       = dateTimeMapperDelegate.setParameter.setParameter
  implicit lazy val setDatetimeOptionParameter = dateTimeMapperDelegate.setParameter.setOptionParameter

  implicit lazy val instantTypeMapper         = instantMapperDelegate.typeMapper
  implicit lazy val getInstantResult          = instantMapperDelegate.getResult.getResult
  implicit lazy val getInstantOptionResult    = instantMapperDelegate.getResult.getOptionResult
  implicit lazy val setInstantParameter       = instantMapperDelegate.setParameter.setParameter
  implicit lazy val setInstantOptionParameter = instantMapperDelegate.setParameter.setOptionParameter

  implicit lazy val localDatetimeTypeMapper         = localDateTimeMapperDelegate.typeMapper
  implicit lazy val getLocalDatetimeResult          = localDateTimeMapperDelegate.getResult.getResult
  implicit lazy val getLocalDatetimeOptionResult    = localDateTimeMapperDelegate.getResult.getOptionResult
  implicit lazy val setLocalDatetimeParameter       = localDateTimeMapperDelegate.setParameter.setParameter
  implicit lazy val setLocalDatetimeOptionParameter = localDateTimeMapperDelegate.setParameter.setOptionParameter

  implicit lazy val localTimeTypeMapper         = localTimeMapperDelegate.typeMapper
  implicit lazy val getLocalTimeResult          = localTimeMapperDelegate.getResult.getResult
  implicit lazy val getLocalTimeOptionResult    = localTimeMapperDelegate.getResult.getOptionResult
  implicit lazy val setLocalTimeParameter       = localTimeMapperDelegate.setParameter.setParameter
  implicit lazy val setLocalTimeOptionParameter = localTimeMapperDelegate.setParameter.setOptionParameter
}

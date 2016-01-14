package com.arcusys.valamis.lrs.jdbc

import javax.inject.{Named, Inject}
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.JodaSupport

import scala.slick.driver.{JdbcProfile, JdbcDriver}
import scala.slick.jdbc.JdbcBackend

/**
  * Created by Iliya Tryapitsin on 04.08.15.
  */
@deprecated
class SimpleSecurityManager (val db: JdbcBackend#Database,
                             val driver: JdbcDriver)
  extends SecurityManager
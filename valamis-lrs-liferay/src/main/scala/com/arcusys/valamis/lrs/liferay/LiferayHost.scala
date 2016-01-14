package com.arcusys.valamis.lrs.liferay

import com.arcusys.valamis.lrs.LrsType
import com.arcusys.valamis.lrs.jdbc.database.SupportedDialect
import com.arcusys.valamis.lrs.liferay.util.ForkJoinPoolWithDbScope
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.InfrastructureUtil

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.slick.driver.JdbcDriver

/**
  * Created by iliyatryapitsin on 16/12/15.
  */
trait LiferayHost {
  private lazy val dataSource = InfrastructureUtil.getDataSource

  lazy val driver   = getSlickDriver
  lazy val db       = driver.profile.backend.Database.forDataSource(dataSource)
  lazy val lrsType  = LrsType.Simple
  lazy val executor = ExecutionContext fromExecutorService ForkJoinPoolWithDbScope

  private def getSlickDriver: JdbcDriver = {
    val connection = dataSource.getConnection
    try {
      val databaseMetaData = connection.getMetaData
      val dbName = databaseMetaData.getDatabaseProductName
      val dbMajorVersion = databaseMetaData.getDatabaseMajorVersion

      SupportedDialect.detectDialect(dbName, dbMajorVersion)
    } finally {
      DataAccess.cleanUp(connection)
    }
  }
}

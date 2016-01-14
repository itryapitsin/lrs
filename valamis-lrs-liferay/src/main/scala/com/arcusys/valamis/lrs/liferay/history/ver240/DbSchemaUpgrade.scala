package com.arcusys.valamis.lrs.liferay.history.ver240

import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.liferay.history.BaseDbUpgrade

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import com.arcusys.valamis.lrs.liferay.{SecurityManager => sm}

/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade (val jdbcDriver: JdbcDriver,
                                 val database: JdbcBackend#Database,
                                 val lrsDataContext: JdbcLrs) extends BaseDbUpgrade {
  val authDataContext  = sm //new SimpleSecurityManager(database, jdbcDriver)
  val applications = authDataContext.applications.baseTableRow
  val tokens       = authDataContext.tokens      .baseTableRow

  val agentProfiles = lrsDataContext.agentProfiles.baseTableRow

  val tablesInMigration = Seq(
    applications.tableName,
    tokens      .tableName
  )

  def hasNotTables = tables
    .map { t => t.name.name }
    .intersect { tablesInMigration }
    .isEmpty

  def upgradeMigrations =
    applications.create.addColumns &
    tokens      .create.addColumns &
    applications.addForeignKeys    &
    tokens      .addForeignKeys    &
    applications.addIndexes        &
    tokens      .addIndexes        &
    agentProfiles.dropForeignKeys  &
    agentProfiles.addForeignKeys
    

  def downgradeMigrations =
    dropForeignKey("fk_token2application", "lrs_tokens"   ) &
    dropIndex("idx_app_name"       , "lrs_applications"   ) &
    dropIndex("idx_token"          , "lrs_tokens"         ) &
    dropTable("lrs_applications"                          ) &
    dropTable("lrs_tokens"                                )
}
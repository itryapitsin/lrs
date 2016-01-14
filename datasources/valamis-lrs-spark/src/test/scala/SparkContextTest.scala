package com.arcusys.valamis.lrs.spark.test

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.row.ScoreRow
import com.arcusys.valamis.lrs.spark.SparkExecutionContext
import com.arcusys.valamis.lrs.spark.typemap.SparkJodaSupport
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.DateTime
import org.scalatest._

import scala.slick.driver.MySQLDriver

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
@Ignore
class SparkContextTest extends FlatSpec {

  "Spark context" should "success with SparkExecutionContext" in {
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("ValamisLrs")

    val sc = new SparkContext(conf)
    val sq = new SQLContext(sc)
    val driver = MySQLDriver
    val db = driver.profile.backend.Database.forURL("jdbc:mysql://localhost:3306/lportal62cega4?user=root")
    val ec = new SparkExecutionContext (driver, db, sq)
    val jodaSupport = new SparkJodaSupport(driver)

    val lrs = new SimpleLrs(
      db,
      ec,
      jodaSupport
    )

    import ec._
    import ec.driver.simple._
    import jodaSupport._

    val statementDF       = lrs.statements          dataFrame
    val actorDF           = lrs.actors              dataFrame
    val accountDF         = lrs.accounts            dataFrame
    val activityDF        = lrs.activities          dataFrame
    val activityProfileDF = lrs.activityProfiles    dataFrame
    val agentProfileDF    = lrs.agentProfiles       dataFrame
    val attachmentDF      = lrs.attachments         dataFrame
    val contextActivityDF = lrs.contextActivities   dataFrame
    val contextDF         = lrs.contexts            dataFrame
    val documentDF        = lrs.documents           dataFrame
    val resultDF          = lrs.results             dataFrame
    val scoreDF           = lrs.scores              dataFrame
    val statementObjectDF = lrs.statementObjects    dataFrame
    val stateProfileDF    = lrs.stateProfiles       dataFrame
    val statementRefDF    = lrs.statementReferences dataFrame
    val subStatementDF    = lrs.subStatements       dataFrame


    val selectListQuery = lrs.statements filter { x => x.stored < DateTime.now } map { x => (x.version, x.resultKey) }

    ec from selectListQuery select
  }
}

//package com.arcusys.valamis.lrs.spark
//
//import java.net.URI
//import javax.inject.Named
//
//import com.arcusys.valamis.lrs.jdbc.{ExecutionContext, JdbcLrs}
//import org.apache.spark.SparkContext
//import org.apache.spark.sql.SQLContext
//import org.joda.time.DateTime
//import scala.reflect.ClassTag
//
///**
// * Created by Iliya Tryapitsin on 20.07.15.
// */
//class SparkLrs (sc:      SparkContext,
//                jdbcLrs: JdbcLrs,
//                @Named("Extended") val executionContext: ExecutionContext) {
//
////  val driver     = executionContext.driver
////  val db         = jdbcLrs.db
//  val sqlContext = new SQLContext(sc)
//
//  val statementDF       = jdbcLrs.statements          dataFrame
//  val actorDF           = jdbcLrs.actors              dataFrame
//  val accountDF         = jdbcLrs.accounts            dataFrame
//  val activityDF        = jdbcLrs.activities          dataFrame
//  val activityProfileDF = jdbcLrs.activityProfiles    dataFrame
//  val agentProfileDF    = jdbcLrs.agentProfiles       dataFrame
//  val attachmentDF      = jdbcLrs.attachments         dataFrame
//  val contextActivityDF = jdbcLrs.contextActivities   dataFrame
//  val contextDF         = jdbcLrs.contexts            dataFrame
//  val documentDF        = jdbcLrs.documents           dataFrame
//  val resultDF          = jdbcLrs.results             dataFrame
//  val scoreDF           = jdbcLrs.scores              dataFrame
//  val statementObjectDF = jdbcLrs.statementObjects    dataFrame
//  val stateProfileDF    = jdbcLrs.stateProfiles       dataFrame
//  val statementRefDF    = jdbcLrs.statementReferences dataFrame
//  val subStatementDF    = jdbcLrs.subStatements       dataFrame
//
//  /**
//   * Get Tincan Verb amount since date time
//   * @param start Start date time
//   * @param verb Verb URI
//   * @return Count of found verbs
//   */
//  def verbAmount(start: Option[DateTime] = None,
//                 verb:  Option[URI] = None)(implicit classTag: ClassTag[String]): Long = {
//    val query = jdbcLrs.statementsCountByFromAndVerb(start, verb)
//    runQuery(query)
////    0
//    ???
//  }
//
//}

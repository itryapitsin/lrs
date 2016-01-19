package com.arcusys.valamis.lrs.liferay.test


import java.net.URI

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.serializer._
import org.openlrs.test.tincan.{Agents, Verbs}
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.json.JsonHelper
import org.joda.time.DateTime
import org.openlrs.StatementQuery
import org.openlrs.serializer.{StatementSerializer, ContextSerializer, AgentSerializer, ActivitySerializer}
import org.openlrs.xapi.{Context, Statement, Agent, Activity}
import org.scalatest._
import scala.util._

/**
 * Created by Iliya Tryapitsin on 25.06.15.
 */
class BaseQueriesSpec (module: BaseCoreModule)
  extends BaseDatabaseSpec (module)
  with FeatureSpecLike {


  agents foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)

      lrs.db withTransaction { implicit session =>
        import lrs._
        import lrs.executionContext.driver.simple._

        lrs.actors add agent
      }
    }
  }

  agents foreach { pair =>
    scenario (pair._1 + " and find it") {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)

      lrs.db withTransaction { implicit session =>
        import lrs._

        val k = lrs.actors keyFor agent
        assert(k isDefined)
      }
    }
  }

  statements foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)

      lrs.db withTransaction { implicit session =>
        import lrs._
        import lrs.executionContext.driver.simple._

        val k = lrs.statements add statement

        val result = lrs.findStatementById(k)
        k
      } afterThat { statementKey =>
        logger.info(s"Statement key = $statementKey")
      }
    }
  }

  contexts foreach { pair =>
    scenario (pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val context = JsonHelper.fromJson[Context](json, new ContextSerializer)

      lrs.db withTransaction { implicit session =>
        import lrs._
        lrs.contexts add context

      } afterThat { key =>
        logger.info(s"Context key = $key")
      }
    }
  }

  activities foreach { pair =>
    scenario(pair._1) {
      val json = JsonHelper.toJson(pair._2.asInstanceOf[AnyRef])
      val activity = JsonHelper.fromJson[Activity](json, new ActivitySerializer)

      lrs.db withTransaction { implicit session =>
        import lrs._
        import lrs.executionContext.driver.simple._

        val key = lrs.activities addUnique activity
        val activities = lrs.activities.filter { x =>
          x.id === activity.id
        } run

        assert(activities.length == 1)
        key

      } afterThat { key =>
        logger.info(s"Activity key = $key")
      }
    }
  }

  scenario ("Test statement search") {

    lrs.db withTransaction { implicit session =>
      for (i <- 1 to 50) {
        statements map { case (s, a) =>
          Try {
            val json = JsonHelper.toJson(a.asInstanceOf[AnyRef])
            val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)

            lrs.addStatement(statement)
          } match {
            case Success (_) => 1
            case Failure (e) => 0
          }
        } sum
      }
    }

    val startTime = DateTime.now()
    val result    = lrs findStatements StatementQuery( verb = new URI(Verbs.validUri) ?)
    val endTime   = DateTime.now()
    logger.info(s"Load ${result.seq.size} statements in ${endTime.getMillisOfDay - startTime.getMillisOfDay} ms")

    val json = JsonHelper.toJson(Agents.Good.`should pass agent typical`)
    val agent = JsonHelper.fromJson[Agent](json, new AgentSerializer)
    val result1 = valamisReporter.findMaxActivityScaled(agent, Verbs.validUri )
    assert(result1.length > 0)
  }
}
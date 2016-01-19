package com.arcusys.valamis.lrs.liferay.test

import com.arcusys.valamis.lrs.spark.typemap.SparkJodaSupport
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.{JodaSupport, SimpleJodaSupport}
import org.openlrs.test.config.DbInit
import net.codingwell.scalaguice.ScalaModule
import org.openlrs.LrsType
import org.openlrs.liferay.ConfigModule

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.ExecutionContext.Implicits._
import scala.slick.driver.{JdbcDriver, JdbcProfile}
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
abstract class BaseCoreModule(val dbInit: DbInit) extends ScalaModule {
  override def configure(): Unit = {
    install(new ConfigModule)

    bind [JdbcDriver          ] toInstance dbInit.driver
    bind [JdbcProfile         ] toInstance dbInit.driver
    bind [JdbcBackend#Database] toInstance dbInit.conn
    bind [DbInit              ] toInstance dbInit

    bind [ExecutionContextExecutor].annotatedWithName(LrsType.SimpleName).toInstance(global)

    bind [JodaSupport     ].annotatedWithName (LrsType.SimpleName).to[SimpleJodaSupport     ]
    bind [JdbcLrs         ].annotatedWithName (LrsType.SimpleName).to[SimpleLrs             ]
    bind [Lrs             ].annotatedWithName (LrsType.SimpleName).to[SimpleLrs             ]
    bind [SecurityManager ].annotatedWithName (LrsType.SimpleName).to[SimpleSecurityManager ]
    bind [ValamisReporter ].annotatedWithName (LrsType.SimpleName).to[SimpleValamisReporter ]
    bind [QueryContext].annotatedWithName (LrsType.SimpleName).to[SimpleExecutionContext]

    bind [JodaSupport    ].annotatedWithName (LrsType.ExtendedName).to[SparkJodaSupport     ]
    bind [JdbcLrs        ].annotatedWithName (LrsType.ExtendedName).to[SimpleLrs            ]
    bind [Lrs            ].annotatedWithName (LrsType.ExtendedName).to[SimpleLrs            ]
    bind [SecurityManager].annotatedWithName (LrsType.ExtendedName).to[SimpleSecurityManager]
    bind [ValamisReporter].annotatedWithName (LrsType.ExtendedName).to[SimpleValamisReporter]
  }
}

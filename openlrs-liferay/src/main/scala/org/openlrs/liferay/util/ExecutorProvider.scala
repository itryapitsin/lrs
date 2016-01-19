package org.openlrs.liferay.util

import java.util.concurrent.ExecutorService
import javax.inject.Named
import com.google.inject.{Inject, Provider}
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class ExecutorProvider @Inject()(@Named("Simple") executorService: ExecutorService)
  extends Provider[ExecutionContextExecutor] {

  override def get() = {
    ExecutionContext.fromExecutorService(executorService)
  }
}

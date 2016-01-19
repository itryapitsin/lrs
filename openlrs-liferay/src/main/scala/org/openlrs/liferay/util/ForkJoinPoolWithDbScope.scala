package org.openlrs.liferay.util

import javax.inject.{Inject, Named}

import scala.concurrent.forkjoin.ForkJoinPool

object ForkJoinPoolWithDbScope extends ForkJoinPool {

  override def execute(task: Runnable) {
    super.execute(new Runnable {
      // this runs in current thread
      val scopeValue = LiferayDbContext.getScope

      override def run() = {
        // this runs in sub thread
        LiferayDbContext.setScope(scopeValue)

        task.run()
      }
    })
  }
}

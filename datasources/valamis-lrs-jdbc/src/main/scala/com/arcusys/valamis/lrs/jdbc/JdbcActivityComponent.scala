package com.arcusys.valamis.lrs.jdbc

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.ActivityQueries
import com.arcusys.valamis.lrs.jdbc.database.row.ActivityProfileRow
import com.arcusys.valamis.lrs.BaseComponent
import org.openlrs.ActivityComponent
import org.openlrs.xapi.Activity

/**
  * Created by iliyatryapitsin on 15/01/16.
  */
trait JdbcActivityComponent extends LrsDataContext
with ActivityQueries
with ActivityComponent
with BaseComponent {

  import driver.simple._

  override implicit val activityProfileInsertInvoker = new InsertInvoker[ActivityProfile, Unit] {
    override def invoke(t: ActivityProfile): Unit = db withSession { implicit s =>

      val documentKey =  documents filter { x => x.key === t.documentId.toString } map { x => x.key } firstOption match {
        case Some(key) => key
        case None => throw new NoSuchElementException(s"Documnet not found by ${t.documentId}")
      }

      val activityKey = activities filter { x => x.id === t.activityId } map { x => x.key } firstOption match {
        case Some(key) => key
        case None => throw new NoSuchElementException(s"Activity not found by ${t.activityId}")
      }

      activityProfiles += ActivityProfileRow(activityKey, t.profileId, documentKey)
    }
  }

  override implicit val activityProfileDeleteInvoke = new DeleteInvoker[ActivityProfileDeleteParams] {
    override def invoke(t: ActivityProfileDeleteParams): Int = db withSession {

      findActivityByIdQC (t.activityId) firstOption _ match {
        case Some(a) =>
          activityProfiles filter { x =>
            x.profileId   === t.profileId &&
            x.activityKey === a.key
          } delete

        case _ => 0
      }
    }
  }

  override implicit val selectActivitiesByNameInvoker = new SelectByParamInvoker[Activity, String] {
    override def invoke(param: String): Seq[Activity] = db withSession {
      activities filter { x => x.name === param } run _ map { rec => rec convert }
    }
  }

  override implicit val findByIdActivityInvoker = new FindByIdInvoker[Activity, Activity#Id] {
    override def invoke(id: Activity#Id): Option[Activity] = db withSession {
      findActivityByIdQC (id) firstOption _ map { x => x.convert }
    }
  }
}

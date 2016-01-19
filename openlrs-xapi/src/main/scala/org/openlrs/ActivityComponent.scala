package org.openlrs

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
import org.openlrs.xapi.{Document, Activity}

/**
  * Created by iliyatryapitsin on 14/01/16.
  */
trait ActivityComponent {
  this: BaseComponent =>

  /**
    * @param profileId The profile id associated with this profile.
    * @param activityId The Activity id associated with these states.
    * @param documentId  The document id associated with this profile.
    */
  case class ActivityProfile(activityId: Activity#Id,
                             profileId:  ProfileId,
                             documentId: Document#Id)

  /**
    * @param profileId The profile id associated with this profile.
    * @param activityId The Activity id associated with these states.
    */
  case class ActivityProfileDeleteParams(activityId: Activity#Id,
                                         profileId:  ProfileId)

  implicit val activityProfileInsertInvoker:  InsertInvoker[ActivityProfile, Unit]
  implicit val activityProfileDeleteInvoke:   DeleteInvoker[ActivityProfileDeleteParams]
  implicit val selectActivitiesByNameInvoker: SelectByParamInvoker[Activity, String]
  implicit val findByIdActivityInvoker:       FindByIdInvoker[Activity, Activity#Id]

  class ActivityStorage extends Rep[Activity] {
    def add(activity: Activity)(implicit inv: InsertInvoker[Activity, Activity#Id]) =
      inv invoke activity

    def selectBy(activityName: String)(implicit inv: SelectByParamInvoker[Activity, String]) =
      inv invoke activityName

    def findById(id: Activity#Id)(implicit inv: FindByIdInvoker[Activity, Activity#Id]) =
      inv invoke id
  }

  class ActivityProfileStorage {
    def add(activityProfile: ActivityProfile)(implicit inv: InsertInvoker[ActivityProfile, Unit]) =
      inv invoke activityProfile

    def delete(activityId: Activity#Id,
               profileId:  ProfileId)(implicit inv: DeleteInvoker[ActivityProfileDeleteParams]) =
      inv invoke ActivityProfileDeleteParams(activityId, profileId)
  }

  val activityStorage = new ActivityStorage
  val activityProfileStorage = new ActivityProfileStorage

  /**
    * Remove the specified profile document in the context of the specified Activity and remove document
    * @param activityId The activity id associated with these profiles.
    * @param profileId The profile id associated with this profile.
    */
  def deleteActivityProfile (activityId: Activity#Id,
                             profileId:  ProfileId): Unit =
    activityProfileStorage delete (activityId, profileId)

  /**
    * Loads the complete Activity Object specified.
    * @param activityId The id associated with the Activities to load. (IRI)
    * @return [[Activity]] if found or [[None]]
    */
  def getActivity (activityId: Activity#Id): Option[Activity] =
    activityStorage findById activityId

  /**
    * Loads the Activities: id and title.
    * @param activity The filter name.
    * @return List of [[Activity]]
    */
  def getActivities (activity: String): Seq[Activity] =
    activityStorage selectBy activity

  /**
    * Loads ids of all profile entries for an [[Activity]].
    * If "since" parameter is specified, this is limited to entries that have been stored
    * or updated since the specified timestamp (exclusive).
    * @param activityId The [[Activity.id]] associated with these profiles.
    * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
    * @return List of profile ids
    */
  def getProfileIds(activityId: Activity#Id,
                    since:      Option[DateTime] = None): Seq[ProfileId] =
    since map { dt =>
      ???

    } getOrElse {
      ???
    }
}

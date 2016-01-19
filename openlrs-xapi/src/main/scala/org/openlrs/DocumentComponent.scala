package org.openlrs

import java.util.UUID

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
import org.openlrs.xapi.{Document, ContentType, Agent, Activity}

/**
  * Created by itryapitsin on 18.12.15.
  */
trait DocumentComponent {
  this: BaseComponent with ActorComponent with ActivityComponent =>

  /**
    * @param activityId The activity id associated with these profiles.
    * @param profileId The profile id associated with this profile.
    */
  case class DocumentSearchParams1(activityId: Activity#Id,
                                   profileId:  ProfileId)

  /**
    * @param agent The Agent associated with this state.
    * @param activityId The Activity id associated with this state.
    * @param stateId The id for this state, within the given context.
    * @param registration The registration id associated with this state.
    */
  case class DocumentSearchParams2(agent: Agent,
                                   activityId: String,
                                   stateId: String,
                                   registration: Option[UUID] = None)

  /**
    * @param agent The Agent associated with these states.
    * @param activityId The Activity id associated with these states.
    * @param registration The registration id associated with these states.
    * @param since Only ids of states stored since the specified timestamp (exclusive) are returned.
    */
  case class DocumentSearchParams3(agent:        Agent,
                                   activityId:   Activity#Id,
                                   registration: Option[UUID]     = None,
                                   since:        Option[DateTime] = None)

  /**
    * @param agent The Agent associated with these states.
    * @param profileId The profile id associated with this profile.
    */
  case class DocumentSearchParams4(agent:     Agent,
                                   profileId: ProfileId)

  /**
    * @param agent The Agent associated with these states.
    * @param profileId The profile id associated with this profile.
    */
  case class DocumentDeleteParams1(agent: Agent,
                                   profileId: ProfileId)

  /**
    * @param agent The Agent associated with these states.
    * @param activityId The Activity id associated with these states.
    * @param stateId The id for this state, within the given context.
    * @param registration The registration id associated with this state.
    */
  case class DocumentDeleteParams2(agent:        Agent,
                                   activityId:   Activity#Id,
                                   stateId:      StateId,
                                   registration: Option[UUID])

  /**
    * @param agent The Agent associated with these states.
    * @param activityId The Activity id associated with these states.
    * @param registration The registration id associated with these states.
    */
  case class DocumentDeleteParams3(agent:        Agent,
                                   activityId:   Activity#Id,
                                   registration: Option[UUID])



  implicit val findByParams1Invoker:   FindByParamInvoker[Document, DocumentSearchParams1]
  implicit val findByParams2Invoker:   FindByParamInvoker[Document, DocumentSearchParams2]
  implicit val findByParams3Invoker:   FindByParamInvoker[Document, DocumentSearchParams3]
  implicit val findByParams4Invoker:   FindByParamInvoker[Document, DocumentSearchParams3]

  implicit val documentInsertInvoker:  InsertInvoker[Document, Document#Id]
  implicit val activityInsertInvoker:  InsertInvoker[Activity, Activity#Id]

  implicit val documentUpdateInvoker:  UpdateInvoker[Document]

  implicit val documentDeleteInvoker1: DeleteInvoker[DocumentDeleteParams1]
  implicit val deleteDocumentInvoker2: DeleteInvoker[DocumentDeleteParams2]
  implicit val deleteDocumentInvoker3: DeleteInvoker[DocumentDeleteParams3]

  implicit val selectByParamInvoker:   SelectByParamInvoker[Document#Id, DocumentSearchParams3]

  /**
    * Abstract document storage
    */
  class DocumentStorage {
    def add(document: Document)(implicit inv: InsertInvoker[Document, Document#Id]) = inv invoke document

    def delete(agent: Agent, profileId: ProfileId)(implicit inv: DeleteInvoker[DocumentDeleteParams1]) =
      inv invoke DocumentDeleteParams1(agent, profileId)

    def delete(agent:        Agent,
               activityId:   Activity#Id,
               stateId:      StateId,
               registration: Option[UUID])(implicit inv: DeleteInvoker[DocumentDeleteParams2]) =
      inv invoke DocumentDeleteParams2(agent, activityId, stateId, registration)

    def delete(agent:        Agent,
               activityId:   Activity#Id,
               registration: Option[UUID])(implicit inv: DeleteInvoker[DocumentDeleteParams3]) =
      inv invoke DocumentDeleteParams3(agent, activityId, registration)

    def update(document: Document)(implicit inv: UpdateInvoker[Document]) = inv invoke document
    def findBy(param: DocumentSearchParams1)(implicit inv: FindByParamInvoker[Document, DocumentSearchParams1]) = inv invoke param
    def findBy(param: DocumentSearchParams2)(implicit inv: FindByParamInvoker[Document, DocumentSearchParams2]) = inv invoke param
    def findBy(param: DocumentSearchParams4)(implicit inv: FindByParamInvoker[Document, DocumentSearchParams4]) = inv invoke param
    def selectIds(param: DocumentSearchParams3)(implicit inv: SelectByParamInvoker[Document#Id, DocumentSearchParams3]) = inv invoke param
  }

  val documentStorage = new DocumentStorage

  /**
    * Get the specified profile document in the context of the specified Activity.
    * @return [[Document]] or [[None]] if not found
    */
  def getDocument(param: DocumentSearchParams1): Option[Document] =
    documentStorage findBy param

  /**
    * Stores, fetches, or deletes the document specified by the given stateId that exists
    * in the context of the specified Activity, Agent, and registration (if specified).

    * @return State Content
    */
  def getDocument(param: DocumentSearchParams2): Option[Document] =
      actorStorage has param.agent match {
        case true  => documentStorage findBy param
        case false => None
      }


  /**
    * Fetches ids of all state data for this context (Activity + Agent [ + registration if specified]).
    * If "since" parameter is specified, this is limited to entries that have been stored or
    * updated since the specified timestamp (exclusive).
    * @return List of [[Seq[Document#Id]]
    */
  def getDocuments(param: DocumentSearchParams3): Seq[Document#Id] =

      if (actorStorage has param.agent) documentStorage selectIds param
      else Seq()

  /**
    * Add or Update if exist the specified profile document in the context of the specified Activity.
    * @param doc The new document version
    */
  def addOrUpdateDocument(doc: Document,
                          param: DocumentSearchParams1): Unit =
    documentStorage findBy param match {
      case Some(doc1) => doc1 mergeWith doc afterThat documentStorage.update
      case _ =>
        // Save activity to the storage
        activityStorage add Activity(param.activityId)

        // Save document
        documentStorage add doc

        // Save activity profile
        activityProfileStorage add ActivityProfile(param.activityId, param.profileId, doc.id)
    }


  /**
    * Store the specified profile document in the context of the specified Agent.
    * @param agent The Agent associated with this profile.
    * @param profileId The profile id associated with this profile.
    * @param doc The document of profile
    */
  def addOrUpdateDocument(agent: Agent,
                          profileId: ProfileId,
                          doc: Document): Unit = ???


  /**
    * Add or Update if exist the specified profile document in the context of the specified Activity.
    * @param doc The new document version
    */
  def addOrUpdateDocument(doc:   Document,
                          param: DocumentSearchParams2): Unit =
    documentStorage findBy param match {
      case Some(doc1) => doc1 mergeWith doc afterThat documentStorage.update
      case _          => documentStorage add doc
    }

  /**
    * Get the specified profile document in the context of the specified Agent.
    */
  def getProfileContent(param: DocumentSearchParams4): Option[Document] =
    documentStorage findBy param

  implicit class DocumentExtensions(doc1: Document) {

    /**
      * Merge current document with new
      * @param doc2 Document
      * @return Updated copy of current document
      */
    def mergeWith(doc2: Document): Document = {
      val content = if (doc1.cType == ContentType.Json && doc2.cType == ContentType.Json)
        JsonHelper.combine(doc1.contents, doc2.contents)
      else
        doc2.contents

      doc1.copy(
        updated  = DateTime.now(),
        contents = content,
        cType    = doc2.cType
      )
    }
  }
}
package com.arcusys.valamis.lrs

import java.util.UUID

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

import scalaz.Reader

/**
  * Created by itryapitsin on 18.12.15.
  */
trait DocumentComponent {
  this: BaseComponent =>

  /**
    * @param activityId The activity id associated with these profiles.
    * @param profileId The profile id associated with this profile.
    */
  case class SearchParams1(activityId: Activity#Id,
                           profileId:  ProfileId)

  /**
    *
    * @param agent The Agent associated with this state.
    * @param activityId The Activity id associated with this state.
    * @param stateId The id for this state, within the given context.
    * @param registration The registration id associated with this state.
    */
  case class SearchParams2(agent: Agent,
                           activityId: String,
                           stateId: String,
                           registration: Option[UUID] = None)

  type ActivityProfile = (Activity#Id, ProfileId, Document#Id)

  implicit val hasAgentInvoker:              HasInvoker[Agent]
  implicit val findByParams1Invoker:         FindByParamInvoker[Document, DocumentComponent#SearchParams1]
  implicit val findByParams2Invoker:         FindByParamInvoker[Document, DocumentComponent#SearchParams2]
  implicit val documentInsertInvoker:        InsertInvoker[Document, Document#Id]
  implicit val activityInsertInvoker:        InsertInvoker[Activity, Activity#Id]
  implicit val documentUpdateInvoker:        UpdateInvoker[Document]
  implicit val activityProfileInsertInvoker: InsertInvoker[ActivityProfile, ActivityProfile]

  /**
    * Abstract document storage
    */
  class DocumentStorage {
    def add(document: Document)(implicit inv: InsertInvoker[Document, Document#Id]) = inv add document
    def add(activity: Activity)(implicit inv: InsertInvoker[Activity, Activity#Id]) = inv add activity
    def add(activityProfile: ActivityProfile)(implicit inv: InsertInvoker[ActivityProfile, ActivityProfile]) = inv add activityProfile
    def update(document: Document)(implicit inv: UpdateInvoker[Document]) = inv update document
    def has(agent: Agent)(implicit inv: HasInvoker[Agent]) = inv has agent
    def findBy(param: SearchParams1)(implicit inv: FindByParamInvoker[Document, DocumentComponent#SearchParams1]) = inv findBy param
    def findBy(param: SearchParams2)(implicit inv: FindByParamInvoker[Document, DocumentComponent#SearchParams2]) = inv findBy param
  }

  val documentStorage = new DocumentStorage

  /**
    * Get the specified profile document in the context of the specified Activity.
    * @return [[Document]] or [[None]] if not found
    */
  def getDocument(param: SearchParams1): Option[Document] = Reader { (storage: DocumentStorage) =>

    storage.findBy(param)(findByParams1Invoker)

  } (documentStorage)

  /**
    * Stores, fetches, or deletes the document specified by the given stateId that exists
    * in the context of the specified Activity, Agent, and registration (if specified).

    * @return State Content
    */
  def getDocument(param: SearchParams2): Option[Document] = Reader { (storage: DocumentStorage) =>
    storage has param.agent match {
      case true  => storage findBy param

      case false => None
    }
  } (documentStorage)

  /**
    * Add or Update if exist the specified profile document in the context of the specified Activity.
    * @param doc The new document version
    */
  def addOrUpdateDocument(doc: Document,
                          param: SearchParams1): Unit = Reader { (storage: DocumentStorage) =>
    storage findBy param match {
      case Some(doc1) => doc1 mergeWith doc afterThat storage.update
      case _          =>
        // Save activity to the storage
        storage add Activity(param.activityId)

        // Save document
        storage add doc

        // Save activity profile
        storage add (param.activityId, param.profileId, doc.id)
    }
  }

  /**
    * Store the specified profile document in the context of the specified Agent.
    * @param agent The Agent associated with this profile.
    * @param profileId The profile id associated with this profile.
    * @param doc The document of profile
    */
  def addOrUpdateDocument(agent: Agent,
                          profileId: ProfileId,
                          doc: Document): Unit = Reader { (storage: DocumentStorage) =>

  }


  /**
    * Add or Update if exist the specified profile document in the context of the specified Activity.
    * @param doc The new document version
    */
  def addOrUpdateDocument(doc:   Document,
                          param: SearchParams2): Unit = Reader { (storage: DocumentStorage) =>
    storage findBy param match {
      case Some(doc1) => doc1 mergeWith doc afterThat storage.update
      case _          => storage add doc
    }
  }


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
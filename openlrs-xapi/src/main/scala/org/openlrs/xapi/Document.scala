package org.openlrs.xapi

import java.util.UUID

import org.joda.time.DateTime

/**
 * The Experience API provides a facility for Activity Providers to save arbitrary data in the form of documents,
 * which may be related to an Activity, Agent, or combination of both.
 * @param id Set by AP, unique within the scope of the agent or activity.
 * @param updated When the document was most recently modified.
 * @param contents The contents of the document (arbitrary binary data)
 */
case class Document(id: Document#Id = UUID.randomUUID(),
                    updated: DateTime = new DateTime(),
                    contents: String,
                    cType: Type) {
  type Id = UUID
}
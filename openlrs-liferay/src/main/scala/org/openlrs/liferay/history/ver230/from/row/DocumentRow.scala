package org.openlrs.liferay.history.ver230.from.row

import java.util.UUID
import org.joda.time.DateTime
import org.openlrs.xapi.ContentType

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
case class DocumentRow(key: Long,
                       documentId: UUID,
                       update: DateTime,
                       content: String,
                       contextType: ContentType.Type)

package org.openlrs.serializer

import org.openlrs.xapi.FormatType

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

case class SerializeFormat(Type: FormatType.Type = FormatType.Exact,
                           Lang: String = "")


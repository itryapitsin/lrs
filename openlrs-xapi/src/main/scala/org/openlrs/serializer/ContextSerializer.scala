package org.openlrs.serializer

import java.util.UUID

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, Extraction}
import org.openlrs.xapi.Constants.Tincan
import org.openlrs.xapi.Constants.Tincan.Field._
import org.openlrs.xapi.{Actor, ContextActivities, Group, _}
import org.openlrs.validator.ContextValidator

/**
 * Created by Iliya Tryapitsin on 11/02/15.
 */
class ContextSerializer(formatType: SerializeFormat) extends CustomSerializer[Context](format => ( {
  case jValue: JValue => implicit val jsonFormats = contextSerializers(formatType)

    ContextValidator checkNotNull jValue

    Context(
      jValue .\ (registration)  .extractOpt[UUID],
      jValue .\ (instructor)    .extractOpt[Actor],
      jValue .\ (team)          .extractOpt[Group],
      jValue .\ (Tincan.ContextActivities)   .extractOpt[ContextActivities],
      jValue .\ (revision)      .extractOpt[String].trimAll(),
      jValue .\ (platform)      .extractOpt[String].trimAll(),
      jValue .\ (language)      .extractOpt[String].trimAll(),
      jValue .\ (Tincan.Statement)     .extractOpt[StatementReference],
      jValue .\ (extensions)    .extractOpt[ExtensionMap]
    )
}, {
  case c: Context => render(Extraction.decompose(c)(contextSerializers(formatType)))
})) {
  def this() = this(SerializeFormat())
}
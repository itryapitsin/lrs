package org.openlrs.serializer

import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction}
import org.openlrs.xapi.Constants.Tincan.Field._
import org.openlrs.xapi.{Constants, Verb}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object VerbSerializer extends CustomSerializer[Verb](format => ( {
  case jValue: JValue =>
    implicit val f = DefaultFormats

    VerbValidator checkNotNull jValue

    val url = jValue.\(Id).extract[String]

    val languages = jValue \ display match {
      case JNothing   => LanguageMap()
      case v: JValue  => v.extract[LanguageMap]
    }

    VerbValidator checkRequirements Verb(url, languages)
}, {
  case verb: Verb =>
    render(Extraction.decompose(verb)(DefaultFormats))
}))

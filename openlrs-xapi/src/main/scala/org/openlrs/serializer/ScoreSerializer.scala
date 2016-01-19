package org.openlrs.serializer

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction}
import org.openlrs.xapi.Constants.Tincan.Field._
import org.openlrs.xapi.{Constants, Score}
import org.openlrs.validator.ScoreValidator

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object ScoreSerializer extends CustomSerializer[Score](implicit format => ( {
  case jValue: JValue =>
    ScoreValidator checkNotNull jValue
    Score(
      jValue.\(scaled).extractOpt[String].map(s => s.toFloat),
      jValue.\(raw)   .extractOpt[String].map(s => s.toFloat),
      jValue.\(min)   .extractOpt[String].map(s => s.toFloat),
      jValue.\(max)   .extractOpt[String].map(s => s.toFloat)
    )
}, {
  case score: Score => render(Extraction.decompose(score)(DefaultFormats))
}))
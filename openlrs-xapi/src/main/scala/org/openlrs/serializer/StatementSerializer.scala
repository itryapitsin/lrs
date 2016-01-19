package org.openlrs.serializer

import org.joda.time.DateTime
import org.json4s.JsonAST._
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, Extraction}
import org.openlrs.exception.IncorrectStatementVersionException
import org.openlrs.xapi.Constants.Tincan
import org.openlrs.xapi.Constants.Tincan.Field._
import org.openlrs.xapi._
import org.openlrs.validator.StatementValidator

//
// Created by Iliya Tryapitsin on 29/12/14.
//
class StatementSerializer(formatType: SerializeFormat) extends CustomSerializer[Statement](format => ({
  case jValue: JValue =>
    implicit val jsonFormats = statementSerializers(formatType)

    StatementValidator checkNotNull jValue

    val r = jValue \ Tincan.Result match {
      case v: JObject => v.extractOpt [Result]
      case _          => None
    }

    val v = jValue \ version match {
      case JNothing   => None
      case v: JString => Some(v.extract [TincanVersion.Type])
      case _          => throw new IncorrectStatementVersionException
    }
    val id = Some (jValue .\(Id) .getUuid)
    StatementValidator checkRequirements Statement(
      id,
      jValue  .\(Tincan.Actor).extract    [Actor],
      jValue  .\(Tincan.Verb) .extract    [Verb],
      jValue  .\(`object`)    .extract    [StatementObject],
      r,
      jValue  .\(Tincan.Field.ContextField).extractOpt [Context],
      jValue  .\(Timestamp)   .getDateTimeOption.getOrElse(DateTime.now),
      jValue  .\(Stored)      .getDateTimeOption.getOrElse(DateTime.now),
      jValue  .\(authority)   .extractOpt [Actor],
      v,
      jValue  .\(Attachments) .extract    [Seq[Attachment]]
    )
}, {
  case statement: Statement =>
    render(Extraction
      .decompose(statement)(statementSerializers(formatType))
      .transformField(fieldTransformer)
    )
})) {
  def this() = this(SerializeFormat())
}
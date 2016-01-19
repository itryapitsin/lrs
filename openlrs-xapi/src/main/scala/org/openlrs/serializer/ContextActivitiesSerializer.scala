package org.openlrs.serializer

import org.json4s.JsonAST.{JArray, JNothing, JObject, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction, Formats}
import org.openlrs.xapi.{ActivityReference, ContextActivities, StatementObjectType}

class ContextActivitiesSerializer extends CustomSerializer[ContextActivities](format => ( {
  case jValue: JValue =>
    implicit val jsonFormats: Formats = DefaultFormats +
      new StatementObjectSerializer() +
      new EnumNameIgnoreCaseSerializer(StatementObjectType)

    val parent = jValue \ "parent" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val grouping = jValue \ "grouping" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val category = jValue \ "category" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val other = jValue \ "other" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }

    ContextActivities(parent, grouping, category, other)
}, {
  case contextActivities: ContextActivities =>
    implicit val jsonFormats: Formats = DefaultFormats +
      new StatementObjectSerializer() +
      new EnumNameIgnoreCaseSerializer(StatementObjectType)

    render(Extraction.decompose(contextActivities)(jsonFormats))
}))

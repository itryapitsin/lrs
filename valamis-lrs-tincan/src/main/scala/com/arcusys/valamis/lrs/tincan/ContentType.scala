package com.arcusys.valamis.lrs.tincan

/**
 * Created by iliyatryapitsin on 26/12/14.
 */

object ContentType extends Enumeration {
  type Type = Value

  val Json = Value(Constants.Content.Json)
  val Other = Value(Constants.Content.Other)

  def apply(value:String) = if(value.isEmpty) ContentType.Other
  else if(value.toLowerCase.contains("json")) ContentType.Json
  else ContentType.Other

}
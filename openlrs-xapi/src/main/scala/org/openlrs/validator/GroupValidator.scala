package org.openlrs.validator

import org.openlrs.xapi.Constants
import Constants.Tincan.Field._
import org.json4s.JsonAST.JValue
import org.openlrs.xapi.Constants

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object GroupValidator {
  def checkNotNull(jValue: JValue) = {
    ActorValidator checkNotNull jValue

    jValue \ member notNull
  }
}

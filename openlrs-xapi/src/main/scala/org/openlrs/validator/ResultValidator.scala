package org.openlrs.validator

import org.openlrs.xapi.Constants
import Constants.Tincan.Field._
import Constants.Tincan._
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ResultValidator {
  def checkNotNull(jValue: JValue) = {
    jValue \ Score      notNull

    jValue \ success    notNull

    jValue \ completion notNull

    jValue \ response   notNull

    jValue \ duration   notNull

    jValue \ extensions notNull
  }

}

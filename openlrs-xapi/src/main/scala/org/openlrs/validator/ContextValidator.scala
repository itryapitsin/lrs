package org.openlrs.validator

import org.json4s.JsonAST.JValue
import org.openlrs.xapi.{Context, Constants}

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ContextValidator {
  import Constants.Tincan.Field._
  import Constants.Tincan._

  def checkNotNull(jValue: JValue) = {
    jValue \ registration   notNull

    jValue \ instructor     notNull

    jValue \ team           notNull

    jValue \ ContextActivities   notNull

    jValue \ revision      notNull

    jValue \ platform      notNull

    jValue \ language      notNull

    jValue \ Statement     notNull

    jValue \ extensions    notNull
  }

  def checkRequirements(cntx: Context) = cntx.language match {
    case Some(v) if  v.isEmpty  => throw new IllegalArgumentException
    case Some(v) if !v.isEmpty  => cntx
    case None                   => cntx
  }
}

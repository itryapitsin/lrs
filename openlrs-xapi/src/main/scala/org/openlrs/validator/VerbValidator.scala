package org.openlrs.validator

import org.openlrs.exception.VerbInvalidException
import org.openlrs.xapi.{Verb, Constants}
import Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.LanguageMapValidator
import org.json4s.JsonAST.JValue
import org.openlrs.util.IRI

import scala.util.{Success, Failure, Try}

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object VerbValidator {
  def checkNotNull(jValue: JValue) = {
    jValue \ Id notNull

    jValue \ display notNull
  }

  def checkRequirements(v: Verb): Verb = {
    v.display.foreach { lang =>
      Try {
        LanguageMapValidator checkRequirements lang
      } match {
        case Failure(_) => throw new VerbInvalidException(v.id)
        case Success(_) =>
      }
    }

    Try(new IRI(v.id)) match {
      case Failure(_) => throw new VerbInvalidException(v.id)
      case Success(_) => v
    }
  }
}

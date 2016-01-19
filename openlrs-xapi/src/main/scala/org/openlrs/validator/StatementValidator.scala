package org.openlrs.validator

import org.openlrs.exception.IncorrectVoidedStatementException
import org.openlrs.xapi.{StatementReference, Statement, Constants}
import Constants.Tincan
import Constants.Tincan.Field._
import Constants.Tincan._
import com.arcusys.valamis.lrs.tincan._
import org.json4s.JsonAST.JValue

object StatementValidator {

  def checkNotNull(jValue: JValue) = {

    jValue \ Id                   notNull

    jValue \ Actor                notNull

    jValue \ Tincan.Verb          notNull

    jValue \ `object`             notNull

    jValue \ Tincan.Result        notNull

    jValue \ Tincan.Field.ContextField notNull

    jValue \ Timestamp            notNull

    jValue \ Stored               notNull

    jValue \ authority            notNull

    jValue \ version              notNull

    jValue \ Attachments          notNull
  }

  def checkRequirements(v: Statement) = {
    if(v.verb.isVoided && !v.obj.isInstanceOf[StatementReference] )
      throw new IncorrectVoidedStatementException("statement verb voided does not use object \"StatementRef\"")

    v.attachments.foreach { attachment =>
      LanguageMapValidator checkRequirements attachment.display
      LanguageMapValidator checkRequirements attachment.description
    }

    v
  }

}

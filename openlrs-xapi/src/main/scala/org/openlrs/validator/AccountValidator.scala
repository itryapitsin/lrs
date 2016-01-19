package org.openlrs.validator

import org.openlrs.xapi.{Constants, Account}
import Constants.Tincan.Field._
import org.apache.commons.validator.routines.UrlValidator
import org.json4s.JsonAST.JValue
import org.openlrs.RunningMode
import org.openlrs.xapi.Account

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object AccountValidator {

  def checkNotNull(jValue: JValue) = {
    jValue \ name     notNull

    jValue \ homePage notNull
  }

  def check(account: Account): Account = {

    val urlValidator = RunningMode.current match {
      case RunningMode.Development => new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS)
      case RunningMode.Production  => new UrlValidator
    }

    if (account.homePage.isEmpty && account.name.isEmpty)
      throw new IllegalArgumentException("Account homepage and Account.name is empty")

    if (!urlValidator.isValid(account.homePage))
      throw new IllegalArgumentException("Account homePage: incorrect URI")

    account
  }
}

package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by Iliya Tryapitsin on 03/01/15.
 */
case class AccountRow(key: AccountRow#KeyType = None,
                      homepage: String,
                      name: String) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}
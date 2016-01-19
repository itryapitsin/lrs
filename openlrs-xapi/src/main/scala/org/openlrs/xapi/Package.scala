package org.openlrs

package object xapi {
  type StringStringMap = Map[String, String]

  type LanguageMap  = StringStringMap
  type ExtensionMap = StringStringMap
  type ProfileId    = String
  type StateId      = String

  object LanguageMap {
    def apply() = Map[String, String]()
  }

  object ExtensionMap {
    def apply() = Map[String, String]()
  }
}

package org.openlrs.xapi

object FormatType extends Enumeration {
  type Type = Value

  val Exact = Value(Constants.Exact)
  val Ids = Value(Constants.Ids)
  val Canonical = Value(Constants.Canonical)
}


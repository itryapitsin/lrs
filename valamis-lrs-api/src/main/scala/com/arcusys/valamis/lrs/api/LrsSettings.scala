package com.arcusys.valamis.lrs.api

import javax.xml.bind.DatatypeConverter

case class LrsSettings(address: String, version: String, auth: LrsAuthSettings)

trait LrsAuthSettings {
  def getAuthString: String
}

case class LrsAuthDefaultSettings(authLine: String) extends LrsAuthSettings {
  override def getAuthString = authLine
}

case class LrsAuthBasicSettings(login: String, password: String) extends LrsAuthSettings {
  override def getAuthString = "Basic " + DatatypeConverter.printBase64Binary((login + ":" + password).getBytes)
}

case class LrsAuthOAuthSettings(authLine: String) extends LrsAuthSettings {
  override def getAuthString = "OAuth " + authLine
}

case class LrsAuthOAuth2Settings(token: String) extends LrsAuthSettings {
  override def getAuthString = "Bearer " + token
}
package com.arcusys.valamis.lrs.api.test

import javax.xml.bind.DatatypeConverter

import com.arcusys.valamis.lrs.api.{LrsAuthOAuthSettings, LrsAuthBasicSettings, LrsSettings}
import com.arcusys.valamis.lrs.tincan.TincanVersion
import org.apache.http.client.utils.URIBuilder

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
trait BaseFeatureTests {
  val apiVersion = TincanVersion.ver10
  val login      = "aadfac5c-9a13-4cf6-af8b-24bbd0a1e280"
  val pass       = "92ac2541-2c1c-48b7-ac5e-e3387e313473"

  val uriBuilder = new URIBuilder()
    .setScheme("https")
    .setHost("stroke.simcenter.duke.edu")
    .setPort(80)

  implicit val lrs = new LrsSettings(
    address = "https://stroke.simcenter.duke.edu/valamis-lrs-portlet/xapi/",
    version = apiVersion.toString,
    auth    = LrsAuthBasicSettings(
      login    = login,
      password = pass
    )
  )
}

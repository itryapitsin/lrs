package org.openlrs.liferay.servlet.request

import java.net.URI

import org.openlrs.liferay.exception.InvalidOrMissingArgumentException

import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

trait BaseTincanActivityRequestComponent extends BaseTincanSinceRequestComponent
with BaseTincanDocumentRequestComponent {
  r: BaseLrsRequest =>

  import org.openlrs.liferay.servlet.request.BaseTincanActivityRequestComponent._

  def activityId = Try(URI.create(require(ACTIVITY_ID))) match {
    case Success(value) => value.toString
    case Failure(_) => throw new InvalidOrMissingArgumentException("activityId in not URI")
  }
}

object BaseTincanActivityRequestComponent {
  val ACTIVITY_ID = "activityId"
}
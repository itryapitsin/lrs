package com.arcusys.valamis.lrs.liferay.servlet.request

import com.arcusys.valamis.lrs.tincan.{ContentType, Document}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
trait BaseTincanDocumentRequestComponent {
  r: BaseLrsRequest =>

  def document = Document(contents = r.body, cType = documentContentType)

  def documentContentType = if (isJsonContent)
    ContentType.Json
  else
    ContentType.Other

  def isJsonContent = request.getContentType.startsWith( """application/json""")
}

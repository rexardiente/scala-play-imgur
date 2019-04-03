package models.domain

import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class ErrorResponse(val error: String) extends AnyVal {
  def toJson(): JsValue = Json.toJson(this)
}
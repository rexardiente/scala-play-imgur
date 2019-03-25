package models.domain

import java.util.UUID
import play.api.libs.json._
import models.implicits._

case class ErrorResponse(val error: String) extends AnyVal {
	def toJson(): JsValue = Json.toJson(this)
}
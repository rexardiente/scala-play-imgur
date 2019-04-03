package models.domain

import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class ImageBase64(name: String, value: String, imageType: String) {
	def toJson(): JsValue = Json.toJson(this)
}

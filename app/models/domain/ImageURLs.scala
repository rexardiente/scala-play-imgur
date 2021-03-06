package models.domain

import java.util.UUID
import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class ImageURLs(id: UUID, urls: Seq[String]) {
	def toJson(): JsValue = Json.toJson(this)
}

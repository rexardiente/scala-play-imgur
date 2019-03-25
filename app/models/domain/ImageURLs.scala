package models.domain

import java.util.UUID
import play.api.libs.json._
import models.implicits._

case class ImageURLs(id: UUID, urls: Seq[String]) {
	def toJson(): JsValue = Json.toJson(this)
}

object ImageURLs {
	def apply(x: String, y: Seq[String]): ImageURLs = apply(UUID.fromString(x), y)
}
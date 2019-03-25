package models.domain

import play.api.libs.json._
import models.implicits._

case class Download(locations: Seq[String]) {
	def toJson(): JsValue = Json.toJson(this)
}
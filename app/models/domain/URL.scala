package models.domain

import play.api.libs.json._
import models.implicits._

case class URL(val value: String) extends AnyVal {
	def toJson(): JsValue = Json.toJson(this)
}
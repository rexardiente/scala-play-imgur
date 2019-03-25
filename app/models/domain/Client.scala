package models.domain

import java.util.UUID
import play.api.libs.json._
import models.implicits._

case class Client(id: UUID, secret: String) {
	def toJson(): JsValue = Json.toJson(this)
}
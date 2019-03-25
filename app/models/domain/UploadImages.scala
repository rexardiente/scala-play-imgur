package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json._
import models.implicits._

case class UploadImages (uploaded: Seq[String]) extends AnyVal {
	// def toJson(): JsValue = Json.toJson(this)
}
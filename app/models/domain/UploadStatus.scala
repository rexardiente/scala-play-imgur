package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json._
import models.implicits._

case class UploadStatus (
		id: UUID,
		created: Instant,
		finished: Option[Instant],
		status: String, // in-progress, completed, Postponed
		uploaded: Uploaded) {
	// def toJson(): JsValue = Json.toJson(this)
}
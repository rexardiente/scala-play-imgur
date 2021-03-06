package models.domain

import java.util.UUID
import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class SubmitResponse(val jobId: UUID) extends AnyVal {
	def toJson(): JsValue = Json.toJson(this)
}

object SubmitResponse {
	def apply(jobId: String): SubmitResponse = apply(UUID.fromString(jobId))
}
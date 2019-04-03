package models.domain

import java.util.UUID
import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class UploadReady(
		code	: String, 
		jobID	: UUID, 
		passed: Seq[ImageBase64], 
		failed: Seq[ImageBase64]) {
	def toJson(): JsValue = Json.toJson(this)
}

object UploadReady {
	def fromInMemoryImagess(
      jobID	: UUID,
      passed: Seq[ImageBase64],
      failed: Seq[ImageBase64]) = {
		new UploadReady("upload_ready", jobID, passed, failed)
	} 
}

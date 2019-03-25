package models.domain

import java.util.UUID
import play.api.libs.json._
import models.implicits._

case class UploadReady(id: UUID, passed: Download, failed: Download) {
	def toJson = Json.toJson(this)
}
 
object UploadReady {
	def apply(id: String, passed: Download, failed: Download): UploadReady = 
		apply(UUID.fromString(id), passed, failed)

	def fromSavedImages(id: UUID, x: Seq[SavedImage], y: Seq[SavedImage]) = 
		new UploadReady(id, Download(x.map(_.info.location)), Download(y.map(_.info.location)))
}
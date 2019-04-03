package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.{ UploadReady, ImageBase64 }

trait UploadReadyJsonFormat {
	protected val uploadReadyReads: Reads[UploadReady] = new Reads[UploadReady] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(UploadReady(
						(v \ "code").as[String],
						(v \ "job_ID").as[UUID],
						(v \ "passed").as[Seq[ImageBase64]],
						(v \ "failed").as[Seq[ImageBase64]]))
				} catch {
					case _: Exception =>
						JsError(JsonValidationError("Cannot De-serialize UploadReady Value."))
				}

			case e => JsError(JsonValidationError("Not Valid UploadReady Object."))
		}
	}

	protected val uploadReadyWrites: Writes[UploadReady] = new Writes[UploadReady] {
		override def writes(v: UploadReady): JsValue =
			Json.obj(
				"code"		-> v.code,
				"job_ID"	-> v.jobID,
				"passed"	-> v.passed,
				"failed"	-> v.failed)
	}

	implicit val uploadReadyFormat: Format[UploadReady] =
		Format(uploadReadyReads, uploadReadyWrites)
}

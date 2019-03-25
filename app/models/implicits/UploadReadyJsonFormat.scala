package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.{ UploadReady, Download }

trait UploadReadyJsonFormat {
	protected val uploadReadyReads: Reads[UploadReady] = new Reads[UploadReady] {
		override def reads(js: JsValue) = js match {
			case v: JsObject => 
				try {
					JsSuccess(UploadReady(
						(v \ "client_ID").as[String],
						(v \ "passed").as[Download],
						(v \ "failed").as[Download]
					))
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
				"client_ID"	-> v.id,
				"passed"	-> v.passed,
				"failed"	-> v.failed
			)
	}

	implicit val uploadReadyFormat: Format[UploadReady] = 
		Format(uploadReadyReads, uploadReadyWrites)
}
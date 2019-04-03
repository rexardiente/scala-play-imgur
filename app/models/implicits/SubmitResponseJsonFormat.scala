package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.SubmitResponse

trait SubmitResponseJsonFormat {
	protected val submitResReads: Reads[SubmitResponse] = new Reads[SubmitResponse] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(SubmitResponse((v \ "job_ID").as[String]))
				} catch {
					case e: Exception =>
						JsError(JsonValidationError("Cannot De-serialize SubmitResponse Value."))
				}

			case e => JsError(JsonValidationError("Not Valid SubmitResponse Object."))
		}
	}

	protected val submitResWrites: Writes[SubmitResponse] = new Writes[SubmitResponse] {
		override def writes(v: SubmitResponse): JsValue = Json.obj("job_ID" -> v.jobId)
	}

	implicit val submitResponseFormat: Format[SubmitResponse] =
		Format(submitResReads, submitResWrites)
}

package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.SubmitResponse

trait SubmitResponseJsonFormat {
	protected val submitResponseReads: Reads[SubmitResponse] = new Reads[SubmitResponse] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(SubmitResponse((v \ "jobId").as[String]))
				} catch {
					case e: Exception => 
						JsError(JsonValidationError("Cannot De-serialize SubmitResponse Value."))
				}

			case e => JsError(JsonValidationError("Not Valid SubmitResponse Object."))
		}
	}

	protected val submitResponseWrites: Writes[SubmitResponse] = new Writes[SubmitResponse] {
		override def writes(v: SubmitResponse): JsValue = Json.obj("jobId" -> v.jobId)
	}

	implicit val submitResponseFormat: Format[SubmitResponse] = 
		Format(submitResponseReads, submitResponseWrites)
}
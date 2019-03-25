package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.ErrorResponse

trait ErrorResponseJsonFormat {
	protected val errResReads: Reads[ErrorResponse] = new Reads[ErrorResponse] {
		override def reads(js: JsValue) = js match {
			case v: JsObject => 
				try {
					JsSuccess(ErrorResponse((v \ "error").as[String]))
				} catch {
					case _: Exception => 
						JsError(JsonValidationError("Cannot De-serialize ErrorResponse Value."))
				}

			case e => JsError(JsonValidationError("Not Valid ErrorResponse Object."))
		}
	}

	protected val errResWrites: Writes[ErrorResponse] = new Writes[ErrorResponse] {
		override def writes(v: ErrorResponse): JsValue =
			Json.obj("error"	-> v.error)
	}

	implicit val errResFormat: Format[ErrorResponse] = 
		Format(errResReads, errResWrites)
}
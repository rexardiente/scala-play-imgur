package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.URL

trait URLJsonFormat {
	protected val urlReads: Reads[URL] = new Reads[URL] {
		override def reads(js: JsValue) = js match {
			case v: JsObject => 
				try {
					JsSuccess(URL((v \ "value").as[String]))
				} catch {
					case _: Exception => 
						JsError(JsonValidationError("Cannot De-serialize URL Value."))
				}

			case e => JsError(JsonValidationError("Not Valid URL Object."))
		}
	}

	protected val urlWrites: Writes[URL] = new Writes[URL] {
		override def writes(v: URL): JsValue = Json.obj("value" -> v.value)
	}

	implicit val urlFormat: Format[URL] = 
		Format(urlReads, urlWrites)
}
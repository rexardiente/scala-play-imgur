package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.Subscribe

trait SubscribeJsonFormat {
	private val subscribeReads: Reads[Subscribe] = new Reads[Subscribe] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(Subscribe(
						(v \ "client_ID").as[UUID],
						(v \ "command").as[String]))
				} catch {
					case e: Exception => 
						JsError(JsonValidationError("Cannot De-serialize Subscribe Value."))
				}

			case e => JsError(JsonValidationError("Not Valid Subscribe Object."))
		}
	}

	private val subscribeWrites: Writes[Subscribe] = new Writes[Subscribe] {
		override def writes(v: Subscribe): JsValue = 
			Json.obj("client_ID" -> v.clientID, "command" -> v.command)
	}

	implicit val subscribeFormat: Format[Subscribe] = 
		Format(subscribeReads, subscribeWrites)
}
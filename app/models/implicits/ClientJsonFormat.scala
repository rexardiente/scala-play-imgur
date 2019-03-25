package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.Client

trait ClientJsonFormat {
	protected val clientReads: Reads[Client] = new Reads[Client] {
		override def reads(js: JsValue) = js match {
			case v: JsObject => 
				try {
					JsSuccess(Client(
						(v \ "id").as[UUID],
		        (v \ "secret").as[String]
					))
				} catch {
					case _: Exception => 
						JsError(JsonValidationError("Cannot De-serialize Client Value."))
				}

			case e => JsError(JsonValidationError("Not Valid Client Object."))
		}
	}

	protected val clientWrites: Writes[Client] = new Writes[Client] {
		override def writes(v: Client): JsValue =
			Json.obj(
				"id" -> v.id, 
				"secret"	-> v.secret
			)
	}

	implicit val clientFormat: Format[Client] = 
		Format(clientReads, clientWrites)
}
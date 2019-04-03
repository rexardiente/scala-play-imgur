package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.Images

trait ImagesJsonFormat {
	protected val imagesReads: Reads[Images] = new Reads[Images] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(Images(
						(v \ "id").as[String],
						(v \ "job_ID").as[UUID],
						(v \ "client_ID").as[UUID],
						(v \ "name").as[String],
						(v \ "datetime").as[Long],
						(v \ "deletehash").as[String],
						(v \ "link").as[String],
						(v \ "height").as[Long],
						(v \ "width").as[Long],
						(v \ "size").as[Long],
		        (v \ "type").as[String]))
				} catch {
					case _: Exception =>
						JsError(JsonValidationError("Cannot De-serialize Images Value."))
				}

			case e => JsError(JsonValidationError("Not Valid Images Object."))
		}
	}

	protected val imagesWrites: Writes[Images] = new Writes[Images] {
		override def writes(v: Images): JsValue =
			Json.obj(
				"id" 				 -> v.id,
				"job_ID" 		 -> v.jobID,
				"client_ID"  -> v.clientID,
				"name" 			 -> v.name,
				"datetime" 	 -> v.datetime,
				"deletehash" -> v.deletehash,
				"link" 			 -> v.link,
				"height" 		 -> v.height,
				"width" 		 -> v.width,
				"size" 			 -> v.size,
				"type"			 -> v.imgType)
	}

	implicit val imagesFormat: Format[Images] =
		Format(imagesReads, imagesWrites)
}

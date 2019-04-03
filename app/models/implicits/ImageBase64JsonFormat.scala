package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.ImageBase64

trait ImageBase64JsonFormat {
	protected val imageBase64Reads: Reads[ImageBase64] = new Reads[ImageBase64] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(ImageBase64(
						(v \ "name").as[String],
						(v \ "value").as[String],
						(v \ "image_type").as[String]))
				} catch {
					case _: Exception =>
						JsError(JsonValidationError("Cannot De-serialize ImageBase64 Value."))
				}

			case e => JsError(JsonValidationError("Not Valid ImageBase64 Object."))
		}
	}

	protected val imageBase64Writes: Writes[ImageBase64] = new Writes[ImageBase64] {
		override def writes(v: ImageBase64): JsValue =
			Json.obj(
				"name"				-> v.name,
				"value"				-> v.value,
				"image_type"	-> v.imageType)
	}

	implicit val imageBase64Format: Format[ImageBase64] =
		Format(imageBase64Reads, imageBase64Writes)
}

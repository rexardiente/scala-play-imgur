package models.implicits

import java.util.UUID
import java.lang.IllegalArgumentException
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.{ ImageURLs, URL }
import utils.RegexCode.{ URL, URLchecker}

trait ImageURLsJsonFormat {
	protected val imageURLsReads: Reads[ImageURLs] = new Reads[ImageURLs] {
		override def reads(js: JsValue) = js match {
			case v: JsObject =>
				try {
					JsSuccess(ImageURLs(
						(v \ "client_ID").as[String],
						{
							val URIs: Seq[String] = (v \ "urls").as[Seq[String]]

							// Now validate Seq[string] to URL if not then throw IllegalArgumentException.
							if (URLchecker(URIs).contains(None))
								throw new IllegalArgumentException("Contains Non-Valid Image URLs.")
							else
								URIs
						}
					))
				} catch {
					case e: Exception => 
						JsError(JsonValidationError("Cannot De-serialize ImageURLs Value."))
				}

			case e => JsError(JsonValidationError("Not Valid ImageURLs Object."))
		}
	}

	protected val imageURLsWrites: Writes[ImageURLs] = new Writes[ImageURLs] {
		override def writes(v: ImageURLs): JsValue = 
			Json.obj(
				"client_ID" -> v.id,
				"urls" -> v.urls
			)
	}

	implicit val imageURLsFormat: Format[ImageURLs] = 
		Format(imageURLsReads, imageURLsWrites)
}
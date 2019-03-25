package models.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.Download

trait DownloadJsonFormat {
	protected val downloadReads: Reads[Download] = new Reads[Download] {
		override def reads(js: JsValue) = js match {
			case v: JsObject => 
				try {
					JsSuccess(Download((v \ "locations").as[Seq[String]]))
				} catch {
					case _: Exception => 
						JsError(JsonValidationError("Cannot De-serialize Download Value."))
				}

			case e => JsError(JsonValidationError("Not Valid Download Object."))
		}
	}

	protected val downloadWrites: Writes[Download] = new Writes[Download] {
		override def writes(v: Download): JsValue =
			Json.obj("locations"	-> v.locations)
	}

	implicit val downloadFormat: Format[Download] = 
		Format(downloadReads, downloadWrites)
}
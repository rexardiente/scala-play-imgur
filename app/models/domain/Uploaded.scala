package models.domain

import play.api.libs.json._
import models.implicits._

case class Uploaded(
		pending: Option[Seq[URL]], 
		complete: Option[Seq[URL]], 
		failed: Option[Seq[URL]]) {	
	// def toJson(): JsValue = Json.toJson(this)
}
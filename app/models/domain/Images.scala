package models.domain

import java.util.UUID
import play.api.libs.json.{ Json, JsValue }
import models.implicits._

case class Images(
    id        : String,
    jobID     : UUID,
    clientID  : UUID,
    name      : String,
    datetime  : Long,
    deletehash: String,
    link      : String,
    height    : Long,
    width     : Long,
    size      : Long,
    imgType   : String) {
	def toJson(): JsValue = Json.toJson(this)
}

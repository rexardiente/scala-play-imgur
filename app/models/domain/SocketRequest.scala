package models.domain

import play.api.libs.json._
import models.implicits._

trait SocketRequest

object SocketRequest {
  implicit def requestsReads: Reads[SocketRequest] = {
    try {
      subscribeFormat.map(x => x: SocketRequest)
    } catch {
      case e: Exception => Reads {
        case _ => JsError(JsonValidationError("Cannot De-serialize SocketRequest value."))
      }
    }
  }

  implicit def requestsWrites = new Writes[SocketRequest] {
    override def writes(node: SocketRequest): JsValue = node match {
      case v: Subscribe => Json.toJson(v)
      case _ => Json.obj("error" -> "wrong Json")
    }
  }
}

package models.domain

import java.util.UUID
import play.api.libs.json._
import models.implicits._

case class Subscribe(clientID: UUID, command: String) extends SocketRequest {
  try {
    require(command == "subscribe")
  } catch {
    case e: Throwable => 
    	throw new IllegalArgumentException("Only subscribe command is allowed.")
  }

  override def toJson: JsValue = Json.toJson(this)
}

object Subscribe {
	def apply(id: String, cmd: String): Subscribe = apply(UUID.fromString(id), cmd)

	def apply(id: UUID): Subscribe = apply(id, "subscribe")
}

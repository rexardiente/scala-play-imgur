package models.domain

import java.util.UUID
import akka.actor.ActorRef
import play.api.libs.json._
import models.implicits._

case class SavedImage(
		id: UUID, 
		actor: Option[ActorRef],
		info: ImageInfo, 
	 	status: Boolean)
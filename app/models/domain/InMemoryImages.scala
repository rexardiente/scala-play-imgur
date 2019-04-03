package models.domain

import java.util.UUID
import akka.actor.ActorRef

case class InMemoryImages(id: UUID, actor: Option[ActorRef], info: ImageInfo, status: Boolean)

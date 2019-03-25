package akka

import java.util.UUID
import scala.collection.mutable.{ HashMap, ListBuffer }
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{ Actor, ActorRef, Props }
import play.api.libs.json.{ JsValue, Json }
import models.domain.{ ErrorResponse, SubmitResponse, Subscribe, SocketRequest }
import models.domain.{ ImageURLs, SavedImage, UploadReady }
import models.implicits._
import utils.Link

object UpdatesManager {
  val onQueJobs  : HashMap[UUID, UUID]          = HashMap()
  val subscribers: HashMap[Subscribe, ActorRef] = HashMap() // User from UI..

  def props[T <: ActorRef](out: T) = Props(classOf[UpdatesManager[T]], out)
}

class UpdatesManager[T <: ActorRef](out: T) extends Actor {
  import UpdatesManager.subscribers

  // overrding default start..
  override def preStart(): Unit = super.preStart()

  def receive(): Receive = {
    case js: JsValue =>
      try {
        self ! js.as[SocketRequest]
      } catch {
        case e: Throwable =>
          out ! ErrorResponse("Not valid request.").toJson
      }

    case v: ImageURLs =>
      // Now, save images to local folder before Uploading back to Imgur.
      // handling duplicated images, If exists then send user that details.
      val grouped = Link.download(v).groupBy(_.status == true)
      val passed  = try { grouped(true)  } catch { case e: Throwable => List.empty }
      val failed  = try { grouped(false) } catch { case e: Throwable => List.empty }

      // Broadcast Images update status..
      out ! UploadReady.fromSavedImages(v.id, passed, failed).toJson

    case v: Subscribe =>
      subscribers += (v -> out) // save user subscription
      out ! SubmitResponse(v.clientID).toJson // send welcome message

    case _ => println("Received other")
  }
}

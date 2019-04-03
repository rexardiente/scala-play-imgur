package akka

import java.util.UUID
import scala.collection.mutable.{ HashMap, ListBuffer }
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{ Actor, ActorRef, Props }
import play.api.libs.json.{ JsValue, Json }
import models.domain._
import models.implicits._
import utils.Link
import models.repo.ImagesRepo

object UpdatesManager {
  val subscribers: HashMap[Subscribe, ActorRef]              = HashMap()
  val onQueJobs  : HashMap[(UUID, UUID), ListBuffer[String]] = HashMap()

  def getOnQueJobs(id: UUID, jobID: UUID): Option[ListBuffer[String]] =
    onQueJobs.lift((id, jobID))

  def addOnQueJobs(id: UUID, jobID: UUID, list: ListBuffer[String]): Unit =
    onQueJobs += ((id, jobID) -> list)

  // returns Option[Option[scala.collection.mutable.ListBuffer[String]]].
  def removeOnQueTx(id: UUID, jobID: UUID, img: String): Option[ListBuffer[String]] =
    getOnQueJobs(id, jobID)
      .map(v => v.find(_ == img).map(x => (v -= x)))
      .flatten

  def removeOnQueJob(id: UUID, jobID: UUID): Unit =
    onQueJobs -= ((id, jobID))

  def props[T <: ActorRef](out: T)(repo: ImagesRepo) =
    Props(classOf[UpdatesManager[T]], out, repo)
}

class UpdatesManager[T <: ActorRef](out: T)(repo: ImagesRepo) extends Actor {
  import UpdatesManager._

  // overriding default start..
  override def preStart(): Unit = super.preStart()

  def receive(): Receive = {
    case js: JsValue =>
      try {
        self ! js.as[SocketRequest]
      } catch {
        case e: Throwable =>
          out ! ErrorResponse("Not valid request.").toJson
      }

    case (jobID: UUID, url:ImageURLs) =>
      // Now, save images to local folder before Uploading back to Imgur.
      // handling duplicated images, If exists then send user that details.
      val downloaded = new Link().download(url).groupBy(_.status == true)

      val passed = try { downloaded(true)  } catch { case e: Throwable => List.empty }
      val failed = try { downloaded(false) } catch { case e: Throwable => List.empty }

      // Convert images to Base64.
      val group1Base64 = passed
        .map(v => ImageBase64(v.info.stringName, v.info.encodedString, v.info.imgTypeStr))
      val group2Base64 = failed
        .map(v => ImageBase64(v.info.stringName, v.info.encodedString, v.info.imgTypeStr))

      // Save to memory for job tracking (id, name).
      addOnQueJobs(url.id, jobID, passed.map(_.info.stringName).to[ListBuffer])

      // Broadcast that are ready to upload (imgur) API.
      out ! UploadReady.fromInMemoryImagess(
              jobID,
              group1Base64, // passed and ready to upload.
              group2Base64  // failed from downloading or not available.
            ).toJson

    case v: Subscribe =>
      // save user subscription.
      subscribers += (v -> out)

      // Send message on connect.
      out ! Json.obj("code" -> "connected")

    case v: UploadServerReponse =>
      val jobID    : UUID             = v.jobID
      val clientID : UUID             = v.id
      val actor    : Option[ActorRef] = subscribers.lift(Subscribe(clientID))
      val imgName  : String           = v.data.$2.name

      // find jobID and remove from memory.
      removeOnQueTx(clientID, jobID, imgName).map { list =>
        actor.get ! Json.obj(
                      "code" -> "image_uploaded",
                      "data" -> v.toJson)
        val image = new Images(
                      v.data.$1.id,
                      v.id,
                      v.jobID,
                      v.data.$2.name,
                      v.data.$1.datetime,
                      v.data.$2.deletehash,
                      v.data.$2.link,
                      v.data.$1.height,
                      v.data.$1.width,
                      v.data.$1.size,
                      v.data.$1.dataType.toString)

        if (list.isEmpty) removeOnQueJob(clientID, jobID)
        // add to web DB.
        repo.insert(image)
      }.getOrElse(None) // Send something if something wrong.

      // Not valid JSON value received.
    case _ => out ! ErrorResponse("Not valid Request.").toJson
  }
}

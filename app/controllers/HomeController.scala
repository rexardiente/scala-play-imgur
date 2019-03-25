package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import akka.actor.{ ActorSystem, ActorRef }
import akka.stream.Materializer
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.{ JsValue, Json }
import models.domain._
import models.implicits._
import models.repo.ClientRepo
import akka.UpdatesManager

// saving images and check if already exists.
import utils.Link 

@Singleton
class HomeController @Inject() (
  clientRepo: ClientRepo,
  cc: ControllerComponents,
  implicit val system: ActorSystem,
  implicit val mat: Materializer) extends AbstractController(cc) {

  def ws() = WebSocket.accept[Any, JsValue] { request =>
    ActorFlow.actorRef(out => UpdatesManager.props(out))
  }

  def index() = Action.async { implicit request: Request[AnyContent] =>
    // imageURLs.urls.map(v => println(v))
    // val client   : Client  = new Client(UUID.randomUUID, "password")
    // val jsClient : JsValue = client.toJson
    // clientRepo.all().map(v => println(s"Clients Lists: $v"))

    Future(Ok("views.html.index()"))
  }

  def images() = TODO

  def upload() = Action.async { implicit request: Request[AnyContent] => 
    request
      .body
      .asJson
      .map(_.asOpt[ImageURLs].map { v =>
        val jobID        : UUID             = UUID.randomUUID        
        val hasSubscribe : Option[ActorRef] = UpdatesManager.subscribers.lift(Subscribe(v.id))
      
        // Subscribers validated, now send data to process download.
        hasSubscribe
          .map { actor => 
            // send message as (ImageURLs).. 
            (system.actorOf(UpdatesManager.props(actor)) ! v)

            Future.successful(Ok(SubmitResponse(jobID).toJson))
          }
          .getOrElse(Future(Ok(ErrorResponse("User has no subscription.").toJson)))
      }
      .getOrElse(Future(BadRequest("Not Valid Image URL/s")))
    ).getOrElse(Future(BadRequest("No URLs Found.")))
  }

  def status(id: java.util.UUID) = TODO // Change to websocket functionality..
}

/* Processes
  - Create image and save then send akka actor after proccess(passed or failed).
  - Save file directory to Memory
  - Upload the file to Imgur then,
  - If fail then send message details of failed file.
  - Else save Image URL to DB(h2) and send transaction details.
  - Remove the file after process either pass or fail.

  ### Akka
  - Create akka socket to back from UI.
  - use the socket to send updates from backend eg. Image upload status, failed, and Success.

  ### Loading Image
  - embedded gif image and send akka to update status. 

  ### Upload Image
  - Ajax request better to setup Client-ID and Secret.
  - Use backend to upload if posible then if not try using Ajax request to upload. 
  - Get server response, return the result to back end.
  - Send appropriate message to User.
*/


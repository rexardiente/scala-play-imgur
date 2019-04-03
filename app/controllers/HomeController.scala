package controllers

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import akka.actor.{ ActorSystem, ActorRef }
import akka.stream.Materializer
import play.api.mvc.{
  AbstractController,
  ControllerComponents,
  WebSocket,
  Action,
  AnyContent,
  Request
}
import play.api.libs.streams.ActorFlow
import play.api.libs.json.{ JsValue, Json }
import models.domain.{ ErrorResponse, ImageURLs, Subscribe, SubmitResponse }
import models.implicits._
import models.repo.ImagesRepo
import akka.UpdatesManager

@Singleton
class HomeController @Inject() (
  imagesRepo: ImagesRepo,
  cc: ControllerComponents,
  implicit val system: ActorSystem,
  implicit val mat: Materializer) extends AbstractController(cc) {

  def ws() = WebSocket.accept[Any, JsValue] { request =>
    ActorFlow.actorRef(out => UpdatesManager.props(out)(imagesRepo))
  }

  def index() = Action.async { implicit request: Request[AnyContent] =>
    Future(Ok(views.html.index()))
  }

  def images() = Action.async { implicit request =>
    imagesRepo.uploaded().map(v => Ok(Json.obj("uploaded" -> v)))
  }

  def upload() = Action.async { implicit request =>
    request.body.asMultipartFormData.map {
      case play.api.mvc.MultipartFormData(a, b, c) =>
        try {
          val data         : ImageURLs        = Json.toJson(a).as[ImageURLs]
          val jobID        : UUID             = UUID.randomUUID
          val hasSubscribe : Option[ActorRef] = UpdatesManager
            .subscribers.lift(Subscribe(data.id))

          hasSubscribe
            .map { actor =>
              // send message as (ImageURLs)..
              system.actorOf(UpdatesManager.props(actor)(imagesRepo)) ! (jobID, data)
              Future.successful(Ok(SubmitResponse(jobID).toJson))
            }
            .getOrElse(Future(Ok(ErrorResponse("User has no subscription.").toJson)))
        } catch {
          case e: Throwable => Future(BadRequest("Not Valid Image URL/s, Please check."))
        }
    }.getOrElse(Future(BadRequest("Not Valid Image URL/s, Please check.")))
  }

  // Removed and changed to WebSocket functionality.
  def status(id: UUID) = TODO
}

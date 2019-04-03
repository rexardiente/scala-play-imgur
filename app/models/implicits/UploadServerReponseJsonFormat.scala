package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.{ UploadServerReponse, Data }

trait UploadServerReponseJsonFormat {
  protected val uploadServerReponseReads: Reads[UploadServerReponse] = new Reads[UploadServerReponse] {
    override def reads(js: JsValue) = js match {
      case v: JsObject =>
        try {
          JsSuccess(UploadServerReponse(
            (v \ "client_ID").as[UUID],
            (v \ "job_ID").as[UUID],
            (v \ "data").as[Data],
            (v \ "success").as[Boolean],
            (v \ "status").as[Int]))
        } catch {
          case e: Exception =>
            JsError(JsonValidationError("Cannot De-serialize UploadServerReponse Value."))
        }

      case e => JsError(JsonValidationError("Not Valid UploadServerReponse Object."))
    }
  }

  protected val uploadServerReponseWrites: Writes[UploadServerReponse] = new Writes[UploadServerReponse] {
    override def writes(v: UploadServerReponse): JsValue =
      Json.obj(
        "client_ID" -> v.id,
        "job_ID"    -> v.jobID,
        "data"      -> v.data,
        "success"   -> v.success,
        "status"    -> v.status)
  }

  implicit val uploadServerReponseFormat: Format[UploadServerReponse] =
    Format(uploadServerReponseReads, uploadServerReponseWrites)
}

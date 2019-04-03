package models.implicits

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.domain.{ UploadServerReponseData1, UploadServerReponseData2, Data }

trait DataJsonFormat {
  def uploadServerReponseData1Reads: Reads[UploadServerReponseData1] = Reads {
    case v: JsObject =>
      try {
        JsSuccess(UploadServerReponseData1(
          (v \ "id").as[String],
          (v \ "title").asOpt[String],
          (v \ "description").asOpt[String],
          (v \ "datetime").as[Long],
          (v \ "type").as[JsString],
          (v \ "animated").as[Boolean],
          (v \ "width").as[Int],
          (v \ "height").as[Int],
          (v \ "size").as[Long],
          (v \ "views").as[Int],
          (v \ "bandwidth").as[Int],
          (v \ "vote").asOpt[String],
          (v \ "favorite").as[Boolean],
          (v \ "nsfw").asOpt[String],
          (v \ "section").asOpt[String],
          (v \ "account_url").asOpt[String],
          (v \ "account_id").as[Int],
          (v \ "is_ad").as[Boolean],
          (v \ "in_most_viral").as[Boolean],
          (v \ "has_sound").as[Boolean],
          (v \ "tags").as[List[String]],
          (v \ "ad_type").as[Int]))
      } catch {
        case other: Exception =>
          JsError(JsonValidationError("Cannot De-serialize UploadServerReponseData1 value."))
      }

    case other => JsError(JsonValidationError("Value is not JSON Object."))
  }

  def uploadServerReponseData1Writes: Writes[UploadServerReponseData1] =
    new Writes[UploadServerReponseData1] {
      override def writes(v: UploadServerReponseData1): JsValue =
        Json.obj(
          "id" -> v.id,
          "title" -> v.title,
          "description" -> v.description,
          "datetime" -> v.datetime,
          "type" -> v.dataType,
          "animated" -> v.animated,
          "width" -> v.width,
          "height" -> v.height,
          "size" -> v.size,
          "views" -> v.views,
          "bandwidth" -> v.bandwidth,
          "vote" -> v.vote,
          "favorite" -> v.favorite,
          "nsfw" -> v.nsfw,
          "section" -> v.section,
          "account_url" -> v.accountURL,
          "account_id" -> v.accountID,
          "is_ad" -> v.isAd,
          "in_most_viral" -> v.inMostViral,
          "has_sound" -> v.hasSound,
          "tags" -> v.tags,
          "ad_type" -> v.adType)
  }

  implicit val uploadServerReponseData1Format: Format[UploadServerReponseData1] =
    Format(uploadServerReponseData1Reads, uploadServerReponseData1Writes)

  def uploadServerReponseData2Reads: Reads[UploadServerReponseData2] = Reads {
    case v: JsObject =>
      try {
        JsSuccess(UploadServerReponseData2(
          (v \ "ad_url").as[String],
          (v \ "edited").as[String],
          (v \ "in_gallery").as[Boolean],
          (v \ "deletehash").as[String],
          (v \ "name").as[String],
          (v \ "link").as[String]))
      } catch {
        case other: Exception =>
          JsError(JsonValidationError("Cannot De-serialize UploadServerReponseData2 value."))
      }

    case other => JsError(JsonValidationError("Value is not JSON Object."))
  }

  def uploadServerReponseData2Writes: Writes[UploadServerReponseData2] =
    new Writes[UploadServerReponseData2] {
      override def writes(v: UploadServerReponseData2): JsValue =
        Json.obj(
          "ad_url" -> v.adURL,
          "edited" -> v.edited,
          "in_gallery" -> v.inGallery,
          "deletehash" -> v.deletehash,
          "name" -> v.name,
          "link" -> v.link)
  }

  implicit val uploadServerReponseData2Format: Format[UploadServerReponseData2] =
    Format(uploadServerReponseData2Reads, uploadServerReponseData2Writes)

  implicit val dataFormat: Format[Data] = (
    (__).format[UploadServerReponseData1] and
    (__).format[UploadServerReponseData2]
  )(Data.apply, unlift(Data.unapply))
}

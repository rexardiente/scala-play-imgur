package models.domain

import java.util.UUID
import play.api.libs.json.{ Json, JsValue, JsString }
import play.api.libs.functional.syntax._
import models.implicits._

case class UploadServerReponseData1(
    id         : String,
    title      : Option[String],
    description: Option[String],
    datetime   : Long,
    dataType   : JsString,
    animated   : Boolean,
    width      : Int,
    height     : Int,
    size       : Long,
    views      : Int,
    bandwidth  : Int,
    vote       : Option[String],
    favorite   : Boolean,
    nsfw       : Option[String],
    section    : Option[String],
    accountURL : Option[String],
    accountID  : Int,
    isAd       : Boolean,
    inMostViral: Boolean,
    hasSound   : Boolean,
    tags       : List[String],
    adType     : Int)

case class UploadServerReponseData2(
    adURL     : String,
    edited    : String,
    inGallery : Boolean,
    deletehash: String,
    name      : String,
    link      : String)

case class Data($1: UploadServerReponseData1, $2: UploadServerReponseData2) {
  def toJson(): JsValue = Json.toJson(this)
}

case class UploadServerReponse(
    id     : UUID,
    jobID  : UUID,
    data   : Data,
    success: Boolean,
    status : Int) extends SocketRequest {
  override def toJson(): JsValue = Json.toJson(this)
}

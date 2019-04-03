package utils

import scala.sys.process._
import models.domain.{ ImageInfo, Subscribe, ImageURLs, InMemoryImages }
import akka.UpdatesManager.subscribers

/*
  validate images and save to DB.
  return clientID, name and status after.
*/
class Link {
  def download(imgs: ImageURLs): Seq[InMemoryImages] = {
    // get account actor reference.
    val subscriber: Option[akka.actor.ActorRef] = subscribers.lift(Subscribe(imgs.id))

    imgs.urls.map { url =>
      val valid = validate(url)

      (InMemoryImages.apply _) tupled (imgs.id, subscriber, valid._1, valid._2)
    }
  }

  def validate(url: String): (ImageInfo, Boolean) = {
    val info: ImageInfo = new ImageInfo(url)

    if (!info.file.exists) {
      new java.net.URL(url) #> info.file !!; // Save image..

      (info, true)
    } else (info, false)
  }
}

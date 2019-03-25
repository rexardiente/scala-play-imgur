package utils

import java.util.UUID
import scala.concurrent.Future
import scala.sys.process._
import akka.actor.ActorRef
import models.domain.{ ImageInfo, Subscribe, ImageURLs, SavedImage }
import akka.UpdatesManager._

object Link {
  // return Image (clientID, name, status)
  def download(img: ImageURLs): Seq[SavedImage] = {
    val wsClient: Option[ActorRef] = subscribers.lift(Subscribe(img.id))

		img.urls.map { url => 
    	val image: ImageInfo = new ImageInfo(url)

    	if (!image.file.exists) {
	      new java.net.URL(url) #> image.file !!; // Save image..

	      new SavedImage(img.id, wsClient, image, true)
	    } else {
	    	new SavedImage(img.id, wsClient, image, false)
	    }
		}
  }
}
package utils

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

object RegexCode {
  val imageName: Regex = raw"([0-9a-zA-Z\._-]+.(png|PNG|gif|GIF|jp[e]?g|JP[E]?G))".r

  val imageType: Regex = raw"((png|PNG|gif|GIF|jp[e]?g|JP[E]?G))".r

 	val url 		 : Regex	= raw"(((http://www)|(https://)|(http://)|(www))[-a-zA-Z0-9@:%_\+.~#?&//=]+)\.(jpg|jpeg|gif|png|bmp|tiff|tga|svg)".r

 	def URLchecker(seq: Seq[String]): Seq[Any] = seq map { v =>
 		url.findFirstMatchIn(v).map(_.toString).getOrElse(None)
 	}
}

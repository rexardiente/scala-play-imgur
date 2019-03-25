package utils

import scala.util.matching.Regex.Match

object RegexCode {
  val IMAGE_NAME = raw"([0-9a-zA-Z\._-]+.(png|PNG|gif|GIF|jp[e]?g|JP[E]?G))".r

 	val URL 			 = raw"((https|http)?:\/\/.*\.(?:png|jpg|jpeg|pdf))".r

 	def URLchecker(seq: Seq[String]): Seq[Any] = seq map { v =>
 		URL.findFirstMatchIn(v).map(_.toString).getOrElse(None)
 	}
}
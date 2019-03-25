package models.domain

import java.io.File
import scala.util.matching.Regex.Match
import utils.RegexCode

final class ImageInfo(v: String) {
  val fileName	 : Option[Match] = 	RegexCode.IMAGE_NAME.findFirstMatchIn(v)
  val stringName : String 			 = 	fileName.map(_.toString).getOrElse("unknown")
  val location	 : String 			 = 	s"public/images/${ stringName }"
  val file			 : File 				 = 	new File(location)
}

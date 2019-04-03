package models.domain

import java.io.File
import java.util.Base64
import scala.util.matching.Regex.Match
import org.apache.commons.io.FileUtils
import utils.RegexCode

final class ImageInfo(v: String) {
  def fileName	   : Option[Match] = RegexCode.imageName.findFirstMatchIn(v)

  def stringName   : String        = fileName.map(_.toString).getOrElse("unknown")

  def imgType      : Option[Match] = RegexCode.imageType.findFirstMatchIn(stringName)

  def imgTypeStr   : String        = imgType.map(_.toString).getOrElse("unknown")

  def location	   : String 			  = s"public/images/${ stringName }"

  def file			   : File 				  = new File(location)

  def byteArray    : Array[Byte]   = FileUtils.readFileToByteArray(file)

  def encodedString: String        = Base64.getEncoder().encodeToString(byteArray)

}

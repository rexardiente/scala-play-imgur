package models.dao

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import models.domain.Images

@Singleton
class ImagesDAO @Inject() (
		protected val dbConfigProvider: DatabaseConfigProvider
	) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  protected class ImagesTable(tag: Tag) extends Table[Images](tag, "IMAGES") {
    def id         = column[String]("ID", O.PrimaryKey)
    def jobID      = column[UUID]("JOB_ID")
    def clientID   = column[UUID]("CLIENT_ID")
    def name       = column[String]("NAME")
    def datetime   = column[Long]("DATE_TIME")
    def deletehash = column[String]("DELETE_HASH")
    def link       = column[String]("LINK")
    def height     = column[Long]("HEIGHT")
    def width      = column[Long]("WIDTH")
    def size       = column[Long]("SIZE")
    def imgType    = column[String]("TYPE")

    def * = (id,
            jobID,
            clientID,
            name,
            datetime,
            deletehash,
            link,
            height,
            width,
            size,
            imgType) <> ((Images.apply _).tupled, Images.unapply)
  }

  def query = new TableQuery(new ImagesTable(_)) {
    @inline def apply(id: String)  = this.withFilter(_.id === id)
  	@inline def apply(img: Images) = this.withFilter(v => v.id === img.id && v.jobID === img.jobID)
  }
}



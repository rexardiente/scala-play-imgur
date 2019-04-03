package models.repo

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import models.domain.Images
import models.dao.ImagesDAO

@Singleton
class ImagesRepo @Inject() (
    dao: ImagesDAO,
		protected val dbConfigProvider: DatabaseConfigProvider
	) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  def get(img: Images): Future[Option[Images]] =
    db.run(dao.query(img).result.headOption)

  def getByID(id: String): Future[Seq[Images]] =
    db.run(dao.query(id).result)

  def insert(img: Images): Future[Int] =
    db.run(dao.query += img)

  def insertMany(img: Seq[Images]): Future[Option[Int]] =
    db.run(dao.query ++= img)

  def remove(id: String): Future[Int] =
    db.run(dao.query(id).delete)

  def update(img: Images): Future[Int] =
    db.run(dao.query(img.id).update(img))

  def all(): Future[Seq[Images]] = db.run(dao.query.result)

  def uploaded(): Future[Seq[String]] = all.map(_.map(_.link))
}



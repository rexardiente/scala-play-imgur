package models.repo

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import models.domain.Client
import models.dao.ClientDAO

@Singleton
class ClientRepo @Inject() (
    dao: ClientDAO,
		protected val dbConfigProvider: DatabaseConfigProvider
	) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  def get(client: Client): Future[Option[Client]] =
    db.run(dao.query(client).result.headOption)

  def getByID(id: UUID): Future[Seq[Client]] =
    db.run(dao.query(id).result)

  def insert(client: Client): Future[Int] =
    db.run(dao.query += client)

  def insertMany(clients: Seq[Client]): Future[Option[Int]] =
    db.run(dao.query ++= clients)

  def remove(id: UUID): Future[Int] =
    db.run(dao.query(id).delete)

  def update(client: Client): Future[Int] =
    db.run(dao.query(client.id).update(client))

  def all(): Future[Seq[Client]] = db.run(dao.query.result)
}



package models.dao

import javax.inject.{ Inject, Singleton }
import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import models.domain.Client

@Singleton
class ClientDAO @Inject() (
		protected val dbConfigProvider: DatabaseConfigProvider
	) (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  protected class ClientTable(tag: Tag) extends Table[Client](tag, "CLIENT") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def secret = column[String]("SECRET")

    def * = (id, secret) <> ((Client.apply _).tupled, Client.unapply)
  }

  def query = new TableQuery(new ClientTable(_)) {
    @inline def apply(id: UUID)   = this.withFilter(_.id === id)
  	@inline def apply(cc: Client) = this.withFilter(v => v.id === cc.id && v.secret === cc.secret)
  }
}



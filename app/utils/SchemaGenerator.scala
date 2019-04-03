package utils

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.jdbc.JdbcProfile
import models.dao.ImagesDAO

@Singleton
class SchemaGenerator @Inject()(
    dao: ImagesDAO,
    protected val dbConfigProvider: DatabaseConfigProvider
  )(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Data Definition Language (DDL) is a computer language for defining data structures.
  def createDDLScript(): Unit = {
    val schemas = dao.query.schema

    val writer = new java.io.PrintWriter("target/schema.sql")
    writer.write("# --- !Ups\n\n")
    schemas.createStatements.foreach { s => writer.write(s + ";\n\n") }

    writer.write("\n\n# --- !Downs\n\n")
    schemas.dropStatements.foreach { s => writer.write(s + ";\n") }

    writer.close()
  }

  createDDLScript()
}

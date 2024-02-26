package configuration

import slick.jdbc.MySQLProfile.api._

import models.Comments.commentsQuery
import models.Tasks.tasksQuery
import models.Users.usersQuery


object DbConnection {
  val db = slick.jdbc.JdbcBackend.Database.forConfig("mydb")

//  def schemaCreation(): Unit = {
//    val createTableAction = usersQuery.schema.create
//    val createTableAction2 = tasksQuery.schema.create
//    val createTableAction3 = commentsQuery.schema.create
//
//    db.run(createTableAction)
//    db.run(createTableAction2)
//    db.run(createTableAction3)
//  }
//  schemaCreation()
}

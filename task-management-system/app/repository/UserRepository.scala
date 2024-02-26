package repository

import Utils.{CreateUserDto, DtoUtils, TaskDto, UserDto}
import configuration.DbConnection
import models.Users.usersQuery
import models.{Task, User, Users}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._

class UserRepository {
  val db = DbConnection.db;

  def createUser(user: User): Future[Option[User]] = {
    val insertUser = (usersQuery returning usersQuery.map(_.id)) += user
    val insertedUserAction = db.run(insertUser);

    insertedUserAction.flatMap{id =>
      val createdUserQuery = usersQuery.filter(_.id === id)
      db.run(createdUserQuery.result.headOption)
    }
  }

  def getAllUsers(): Future[Option[Seq[User]]] = {
    val getUsersQuery = usersQuery.result;
    val getUsersAction: Future[Seq[User]] = db.run(getUsersQuery)
    getUsersAction.map(Some(_))
  }

  def getUserById(id: Long): Future[Option[User]] = {
    val getUserByIdQuery = usersQuery.filter(_.id === id)
    db.run(getUserByIdQuery.result.headOption)
  }

  def updateUserById(user: User, id: Long): Future[Option[User]] = {
    val updateUserQuery = usersQuery.filter(_.id === id)
    val updateUserAction = updateUserQuery.update(user)
    val updatedUserId: Future[Int] = db.run(updateUserAction)

    updatedUserId.flatMap { _ =>
      db.run(updateUserQuery.result.headOption)
    }
  }

  def deleteUserById(id: Long): Future[Int] = {
    val deleteUserQuery = usersQuery.filter(_.id === id).delete
    db.run(deleteUserQuery)
  }

}

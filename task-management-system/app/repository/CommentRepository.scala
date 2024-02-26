package repository

import configuration.DbConnection
import models.Comment
import models.Comments.commentsQuery

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._

class CommentRepository {
  val db = DbConnection.db;

  def createComment(comment: Comment): Future[Option[Comment]] = {
    val insertComment = (commentsQuery returning commentsQuery.map(_.id)) += comment
    val insertedCommentAction = db.run(insertComment);

    insertedCommentAction.flatMap{id =>
      val createdCommentQuery = commentsQuery.filter(_.id === id)
      db.run(createdCommentQuery.result.headOption)
    }
  }

  def getAllComments(): Future[Option[Seq[Comment]]] = {
    val getCommentsQuery = commentsQuery.result;
    val getCommentsAction: Future[Seq[Comment]] = db.run(getCommentsQuery)
    getCommentsAction.map(Some(_))
  }

  def getCommentById(id: Long): Future[Option[Comment]] = {
    val getCommentByIdQuery = commentsQuery.filter(_.id === id)
    db.run(getCommentByIdQuery.result.headOption)
  }

  def updateCommentById(comment: Comment, id: Long): Future[Option[Comment]] = {
    val updateCommentQuery = commentsQuery.filter(_.id === id)
    val updateCommentAction = updateCommentQuery.update(comment)
    val updatedCommentId: Future[Int] = db.run(updateCommentAction)

    updatedCommentId.flatMap { _ =>
      db.run(updateCommentQuery.result.headOption)
    }
  }

  def deleteCommentById(id: Long): Future[Int] = {
    val deleteCommentQuery = commentsQuery.filter(_.id === id).delete
    db.run(deleteCommentQuery)
  }

  def getCommentsByUserId(id: Long): Future[Option[Seq[Comment]]] = {
    val getCommentsByUserQuery = commentsQuery.filter(_.userId === id)
    val getCommentsByUserIdAction: Future[Seq[Comment]] = db.run(getCommentsByUserQuery.result)
    getCommentsByUserIdAction.map(Some(_))
  }

  def getCommentsByTaskId(id: Long): Future[Option[Seq[Comment]]] = {
    val getCommentsByTaskQuery = commentsQuery.filter(_.taskId === id)
    val getCommentsByTaskIdAction: Future[Seq[Comment]] = db.run(getCommentsByTaskQuery.result)
    getCommentsByTaskIdAction.map(Some(_))
  }

}

package models

import java.time.LocalDateTime
import slick.jdbc.MySQLProfile.api._

case class Comment(id: Option[Long], text: String, createdAt: LocalDateTime, userId: Long,taskId: Long)

class Comments(tag: Tag) extends Table[Comment](tag, "comments"){

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def text = column[String]("text")
  def createdAt = column[LocalDateTime]("created_at")
  def userId = column[Long]("user_id")
  def taskId = column[Long]("task_id")

  override def * = (id.?, text,createdAt,userId,taskId) <> ((Comment.apply _).tupled, Comment.unapply)
  def user = foreignKey("users", userId,Users.usersQuery) (_.id)
  def task = foreignKey("tasks", taskId,Tasks.tasksQuery) (_.id)
}

object Comments{
  val commentsQuery = TableQuery[Comments]
}


package Utils

import models.{Comment, Role, Status, Task, User}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads, Writes}

import java.time.LocalDateTime

case class CreateUserDto( username: String, email: String, password: String, role: Role.Role)

case class UserDto(id: Long, username: String, email: String, role: Role.Role)

case class TaskDto(id: Long, name: String, description: String, status: Status.Status,assigneeId: Long)

case class CreateTaskDto(name: String, description: String, status: Status.Status,assigneeId: Long)

case class CommentDto(id: Long, text: String, createdAt: LocalDateTime, userId: Long,taskId: Long)

case class CreateCommentDto(text: String, userId: Long,taskId: Long)

object CreateUserDto{
  implicit val rds: Reads[CreateUserDto] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "role").read[Role.Role]
    )(CreateUserDto.apply _)
}

object CreateTaskDto{
  implicit val rds: Reads[CreateTaskDto] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "status").read[Status.Status] and
      (JsPath \ "assigneeId").read[Long]
    )(CreateTaskDto.apply _)
}

object CreateCommentDto{
  implicit val rds: Reads[CreateCommentDto] = (
    (JsPath \ "text").read[String] and
      (JsPath \ "userId").read[Long] and
      (JsPath \ "taskId").read[Long]
    )(CreateCommentDto.apply _)
}


object UserDto{
  def empty: UserDto = UserDto(0,"","",Role.NULL)

  implicit val writes: Writes[UserDto] = Json.writes[UserDto]
}

object TaskDto{
  def empty: TaskDto = TaskDto(0,"","",Status.NULL,0)

  implicit val writes: Writes[TaskDto] = Json.writes[TaskDto]
}

object CommentDto{
  def empty: CommentDto = CommentDto(0,"",LocalDateTime.now(),0,0)

  implicit val writes: Writes[CommentDto] = Json.writes[CommentDto]
}

object DtoUtils{
  def fromCreateUserDto(userDto: CreateUserDto,id : Long): User = {
    User(Option(id),userDto.username,userDto.email,userDto.password,userDto.role)
  }

  def toUserDto(user: User, id: Long): UserDto = {
    UserDto(id,user.username,user.email,user.role)
  }

  def fromCreateTaskDto(createTaskDto: CreateTaskDto,id: Long): Task = {
    Task(Option(id),createTaskDto.name,createTaskDto.description,createTaskDto.status,createTaskDto.assigneeId);
  }

  def toTaskDto(task: Task,id: Long): TaskDto = {
    TaskDto(id,task.name,task.description,task.status,task.assigneeId)
  }

  def fromCreateCommentDto(createCommentDto: CreateCommentDto,id: Long): Comment = {
    Comment(Option(id),createCommentDto.text,LocalDateTime.now(),createCommentDto.userId,createCommentDto.taskId)
  }

  def toCommentDto(comment: Comment,id: Long): CommentDto = {
    CommentDto(id, comment.text,comment.createdAt,comment.userId,comment.taskId)
  }

}

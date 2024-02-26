package Service

import Utils.{CommentDto, CreateUserDto, DtoUtils, TaskDto, UserDto}
import jakarta.inject.Inject
import models.{Task, User}
import repository.{CommentRepository, TaskRepository, UserRepository}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserService @Inject()(userRepository: UserRepository, taskRepository: TaskRepository,commentRepository: CommentRepository) {

  def createUser(createUserDto: CreateUserDto): Future[UserDto] = {
    val user: User = DtoUtils.fromCreateUserDto(createUserDto,0)
    userRepository.createUser(user).map{
      case Some(user) => DtoUtils.toUserDto(user,user.id.get)
      case None => UserDto.empty
    }
  }

  def getAllUsers(): Future[Seq[UserDto]] = {
    userRepository.getAllUsers().map{
      case Some(users) => users.map(user => DtoUtils.toUserDto(user, user.id.get))
      case None => Seq.empty[UserDto]
    }
  }

  def getUserById(id: Long): Future[UserDto] = {
    userRepository.getUserById(id).map{
      case Some(user) => DtoUtils.toUserDto(user, id);
      case None => UserDto.empty
    }
  }

  def updateUserById(createUserDto: CreateUserDto, id: Long): Future[UserDto] = {
    val user = DtoUtils.fromCreateUserDto(createUserDto,id)
    userRepository.updateUserById(user, id).map{
      case Some(updatedUser) => DtoUtils.toUserDto(updatedUser,id)
      case None => UserDto.empty
    }
  }

  def deleteUserById(id: Long): Future[Boolean] = {
    userRepository.deleteUserById(id).map { rowsAffected =>
      if (rowsAffected > 0) {
        true
      } else {
        false
      }
    }
  }

  def getMyTasks(id: Long): Future[Seq[TaskDto]] = {
    taskRepository.getTasksByUserId(id).map{
      case Some(tasks) => tasks.map(task => DtoUtils.toTaskDto(task, task.id.get))
      case None => Seq.empty[TaskDto]
    }
  }

  def getMyComments(id: Long): Future[Seq[CommentDto]] = {
    commentRepository.getCommentsByUserId(id).map{
      case Some(comments) => comments.map(comment => DtoUtils.toCommentDto(comment, comment.id.get))
      case None => Seq.empty[CommentDto]
    }
  }

}

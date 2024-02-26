package Service

import Utils.{ActiveTaskScheduler, CommentDto, CreateTaskDto, DtoUtils, TaskDto}
import jakarta.inject.Inject
import models.Task
import repository.{CommentRepository, TaskRepository}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TaskService @Inject()(taskRepository: TaskRepository, commentRepository: CommentRepository){

  def createTask(createTaskDto: CreateTaskDto): Future[TaskDto] = {
    val task: Task = DtoUtils.fromCreateTaskDto(createTaskDto,0)
    taskRepository.createTask(task).map{
      case Some(task) => DtoUtils.toTaskDto(task,task.id.get)
      case None => TaskDto.empty
    }
  }

  def getAllTasks(): Future[Seq[TaskDto]] = {
//    taskRepository.getActiveTasks().map{
//      case Some(tasks) => tasks.map(task => println(s"${task.name}"))
//      case None => println("no tasks")
//    }

    taskRepository.getAllTasks().map{
      case Some(tasks) => tasks.map(task => DtoUtils.toTaskDto(task, task.id.get))
      case None => Seq.empty[TaskDto]
    }
  }

  def getTaskById(id: Long): Future[TaskDto] = {
    taskRepository.getTaskById(id).map{
      case Some(task) => DtoUtils.toTaskDto(task, id);
      case None => TaskDto.empty
    }
  }

  def updateTaskById(createTaskDto: CreateTaskDto, id: Long): Future[TaskDto] = {
    val task = DtoUtils.fromCreateTaskDto(createTaskDto,id)
    taskRepository.updateTaskById(task, id).map{
      case Some(updatedTask) => DtoUtils.toTaskDto(updatedTask,id)
      case None => TaskDto.empty
    }
  }

  def deleteTaskById(id: Long): Future[Boolean] = {
    taskRepository.deleteTaskById(id).map { rowsAffected =>
      if (rowsAffected > 0) {
        true
      } else {
        false
      }
    }
  }

  def getCommentsByTaskId(id: Long): Future[Seq[CommentDto]] = {
    commentRepository.getCommentsByTaskId(id).map{
      case Some(comments) => comments.map(comment => DtoUtils.toCommentDto(comment, comment.id.get))
      case None => Seq.empty[CommentDto]
    }
  }

  def getActiveTasks(): Future[Seq[TaskDto]] = {
    taskRepository.getActiveTasks().map{
      case Some(tasks) => tasks.map(task => DtoUtils.toTaskDto(task,task.id.get))
      case None => Seq.empty[TaskDto]
    }
  }

}

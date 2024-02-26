package controllers

import Utils.{ActiveTaskScheduler, CreateTaskDto, CreateUserDto}
import Service.TaskService
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TaskController @Inject()(val controllerComponents: ControllerComponents, taskService: TaskService)
                              (implicit ec: ExecutionContext) extends BaseController{

  def createTask(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateTaskDto].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      createTaskDto => {
        taskService.createTask(createTaskDto).map{ createdTaskDto =>
          Created(Json.toJson(createdTaskDto))
        }
      }
    )
  }

  def getAllTasks: Action[AnyContent] = Action.async { implicit request =>
    taskService.getAllTasks().map { taskDtoList =>
      if (taskDtoList.isEmpty) {
        NotFound
      }
      else {
        Ok(Json.toJson(taskDtoList))
      }
    }
  }

  def getTaskById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    taskService.getTaskById(id).map { taskDto =>
      if (taskDto.id == 0) {
        NotFound
      }
      else {
        Ok(Json.toJson(taskDto))
      }
    }
  }

  def updateTaskById(id: Long): Action[JsValue] =Action.async(parse.json){ implicit request =>
    request.body.validate[CreateTaskDto].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      updateTaskDto => {
        taskService.updateTaskById(updateTaskDto,id).map(updatedTaskDto => Ok(Json.toJson(updatedTaskDto))) recover {
          case _: NoSuchElementException => NotFound
        }
      }
    )
  }

  def deleteTaskById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    taskService.deleteTaskById(id).map{
      case true => Accepted
      case false => NotFound
    }
  }

  def getCommentsByTaskId(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    taskService.getCommentsByTaskId(id).map { commentsDtoList =>
      if (commentsDtoList.isEmpty) {
        NotFound
      }
      else {
        Ok(Json.toJson(commentsDtoList))
      }
    }
  }

}

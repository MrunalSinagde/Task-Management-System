package controllers

import Utils.CreateUserDto
import Service.{TaskService, UserService}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import java.util
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(val controllerComponents: ControllerComponents, userService: UserService)
                                   (implicit ec: ExecutionContext) extends BaseController{

  def createUser(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateUserDto].fold(
      errors => {
      Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      createUserDto => {
        userService.createUser(createUserDto).map{ createdUserDto =>
          Created(Json.toJson(createdUserDto))
       }
     }
    )
  }

  def getAllUsers: Action[AnyContent] = Action.async { implicit request =>
    userService.getAllUsers().map { userDtoList =>
      if (userDtoList.isEmpty) {
        NotFound
      }
      else {
        Ok(Json.toJson(userDtoList))
      }
    }
  }

  def getUserById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    userService.getUserById(id).map{userDto =>
      if(userDto.id == 0){
        NotFound
      }
      else {
        Ok(Json.toJson(userDto))
      }
    }
  }

  def updateUserById(id: Long): Action[JsValue] =Action.async(parse.json){ implicit request =>
    request.body.validate[CreateUserDto].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      updateUserDto => {
        userService.updateUserById(updateUserDto,id).map(updatedUserDto => Ok(Json.toJson(updatedUserDto))) recover {
          case _: NoSuchElementException => NotFound
        }
      }
    )
  }

  def deleteUserById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    userService.deleteUserById(id).map{
      case true => Accepted
      case false => NotFound
    }
  }

  def getAllMyTasks(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    userService.getMyTasks(id).map{ taskDtoList =>
      if(taskDtoList.isEmpty) {
        NotFound
      }
      else {
        Ok(Json.toJson(taskDtoList))
      }
    }
  }

  def getAllMyComments(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    userService.getMyComments(id).map{ commentDtoList =>
      if(commentDtoList.isEmpty){
        NotFound
      }
      else {
        Ok(Json.toJson(commentDtoList))
      }
    }
  }

}

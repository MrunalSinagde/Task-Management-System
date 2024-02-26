package controllers

import Utils.CreateCommentDto
import Service.CommentService
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import play.api.libs.json.{JsError, JsValue, Json}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CommentController @Inject()(val controllerComponents: ControllerComponents, commentService: CommentService)
                                 (implicit ec: ExecutionContext) extends BaseController{

  def createComment(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[CreateCommentDto].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      createCommentDto => {
        commentService.createComment(createCommentDto).map{ createdCommentDto =>
          Created(Json.toJson(createdCommentDto))
        }
      }
    )
  }

  def getAllComments: Action[AnyContent] = Action.async { implicit request =>
    commentService.getAllComments().map{ commentDtoList =>
      if(commentDtoList.isEmpty){
        NotFound
      }
      else{
        Ok(Json.toJson(commentDtoList))
      }
    }
  }

  def getCommentById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    commentService.getCommentById(id).map { commentDto =>
      if(commentDto.id == 0){
        NotFound
      }
        else{
          Ok(Json.toJson(commentDto))
        }
    }
  }

  def updateCommentById(id: Long): Action[JsValue] = Action.async(parse.json){ implicit request =>
    request.body.validate[CreateCommentDto].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      updateCommentDto => {
        commentService.updateCommentById(updateCommentDto,id).map(updatedCommentDto => Ok(Json.toJson(updatedCommentDto))) recover {
          case _: NoSuchElementException => NotFound
        }
      }
    )
  }

  def deleteCommentById(id: Long): Action[AnyContent] = Action.async{ implicit request =>
    commentService.deleteCommentById(id).map{
      case true => Accepted
      case false => NotFound
    }
  }

}

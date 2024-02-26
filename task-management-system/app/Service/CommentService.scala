package Service

import Utils.{CommentDto, CreateCommentDto, DtoUtils}
import jakarta.inject.Inject
import models.Comment
import repository.CommentRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CommentService @Inject()(commentRepository: CommentRepository) {

  def createComment(createCommentDto: CreateCommentDto): Future[CommentDto] = {
    val comment: Comment = DtoUtils.fromCreateCommentDto(createCommentDto, 0)
    commentRepository.createComment(comment).map {
      case Some(comment) => DtoUtils.toCommentDto(comment, comment.id.get)
      case None => CommentDto.empty
    }
  }

  def getAllComments(): Future[Seq[CommentDto]] = {
    commentRepository.getAllComments().map {
      case Some(comments) => comments.map(comment => DtoUtils.toCommentDto(comment, comment.id.get))
      case None => Seq.empty[CommentDto]
    }
  }

  def getCommentById(id: Long): Future[CommentDto] = {
    commentRepository.getCommentById(id).map {
      case Some(comment) => DtoUtils.toCommentDto(comment, id);
      case None => CommentDto.empty
    }
  }

  def updateCommentById(createCommentDto: CreateCommentDto, id: Long): Future[CommentDto] = {
    val comment = DtoUtils.fromCreateCommentDto(createCommentDto, id)
    commentRepository.updateCommentById(comment, id).map {
      case Some(updatedComment) => DtoUtils.toCommentDto(updatedComment, id)
      case None => CommentDto.empty
    }
  }

  def deleteCommentById(id: Long): Future[Boolean] = {
    commentRepository.deleteCommentById(id).map { rowsAffected =>
      if (rowsAffected > 0) {
        true
      } else {
        false
      }
    }
  }

}

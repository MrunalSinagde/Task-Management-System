package repository

import configuration.DbConnection
import models.Tasks.tasksQuery
import models.{Status, Task, Tasks, Users}
import shapeless.Lazy.apply

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._


class TaskRepository {
  val db = DbConnection.db;

  def createTask(task: Task): Future[Option[Task]] = {
    val insertTask = (tasksQuery returning tasksQuery.map(_.id)) += task
    val insertedTaskAction = db.run(insertTask);

    insertedTaskAction.flatMap{id =>
      val createdTaskQuery = tasksQuery.filter(_.id === id)
      db.run(createdTaskQuery.result.headOption)
    }
  }

  def getAllTasks(): Future[Option[Seq[Task]]] = {
    val getTasksQuery = tasksQuery.result;
    val getTasksAction: Future[Seq[Task]] = db.run(getTasksQuery)
    getTasksAction.map(Some(_))
  }

  def getTaskById(id: Long): Future[Option[Task]] = {
    val getTaskByIdQuery = tasksQuery.filter(_.id === id)
    db.run(getTaskByIdQuery.result.headOption)
  }

  def updateTaskById(task: Task, id: Long): Future[Option[Task]] = {
    val updateTaskQuery = tasksQuery.filter(_.id === id)
    val updateTaskAction = updateTaskQuery.update(task)
    val updatedTaskId: Future[Int] = db.run(updateTaskAction)

    updatedTaskId.flatMap { _ =>
      db.run(updateTaskQuery.result.headOption)
    }
  }

  def deleteTaskById(id: Long): Future[Int] = {
    val deleteTaskQuery = tasksQuery.filter(_.id === id).delete
    db.run(deleteTaskQuery)
  }

  def getTasksByUserId(id: Long): Future[Option[Seq[Task]]] = {
    val getTasksByUserQuery = tasksQuery.filter(_.assigneeId === id)
    val getTasksByUserIdAction: Future[Seq[Task]] = db.run(getTasksByUserQuery.result)
    getTasksByUserIdAction.map(Some(_))
  }

  def getActiveTasks(): Future[Option[Seq[Task]]] = {
    val getActiveTasksQuery = tasksQuery.filter(_.status === Status.IN_PROGRESS)
    val getActiveTasksAction = db.run(getActiveTasksQuery.result)
    getActiveTasksAction.map(Some(_))
  }

}

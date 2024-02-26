package models

import models.Status.Status
import play.api.libs.json.{JsError, JsString, JsSuccess, Reads}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{CanBeQueryCondition, Rep}
//import EnumColumnExtension._

object Status extends Enumeration{
  type Status = Value
  val IN_PROGRESS, COMPLETED,NULL = Value

  implicit val reads: Reads[Status] = Reads{
    case JsString("IN_PROGRESS") => JsSuccess(IN_PROGRESS)
    case JsString("COMPLETED") => JsSuccess(COMPLETED)
    case JsString("NULL") => JsSuccess(NULL)
    case _ => JsError("Invalid Role value")
  }

  implicit val statusColumnType: BaseColumnType[Status.Status] = MappedColumnType.base[Status.Status, String](
    e => e.toString,
    s => Status.withName(s)
  )

//  implicit class EnumColumnExtension[E <: Enumeration#Value, T <: Enumeration](column: Rep[E]) {
//    def ===(enum: T#Value)(implicit wt: CanBeQueryCondition[E]): Rep[Boolean] = column === enum.toString
//  }
//  implicit val statusCanBeQueryCondition: CanBeQueryCondition[Status.Value] = CanBeQueryCondition.columnCanBeQueryCondition

}

case class Task(id: Option[Long], name: String, description: String, status: Status.Status,assigneeId: Long)

class Tasks(tag: Tag) extends Table[Task](tag, "tasks"){

  implicit val
  statusColumnType: JdbcType[Status] with BaseTypedType[Status] = MappedColumnType.base[Status.Status, String](
    e => e.toString,
    s => Status.withName(s)
  )

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def status = column[Status.Status]("status")
  def assigneeId = column[Long]("assignee_id")

  override def * = (id.?, name,description,status,assigneeId) <> ((Task.apply _).tupled, Task.unapply)
  def assignee = foreignKey("users", assigneeId,Users.usersQuery) (_.id)
}

object Tasks {
  val tasksQuery = TableQuery[Tasks]
}






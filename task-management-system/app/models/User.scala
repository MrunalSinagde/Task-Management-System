package models

import slick.jdbc.MySQLProfile.api._
import play.api.libs.json._

object Role extends Enumeration{
  type Role = Value
  val Admin, User,NULL = Value

  implicit val reads: Reads[Role] = Reads{
    case JsString("Admin") => JsSuccess(Admin)
    case JsString("User") => JsSuccess(User)
    case JsString("NULL") => JsSuccess(NULL)
    case _ => JsError("Invalid Role value")
  }
}

case class User(id: Option[Long], username: String, email: String, password: String, role: Role.Role)

class Users(tag: Tag) extends Table[User](tag, "users") {
  implicit val roleColumnType = MappedColumnType.base[Role.Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def email = column[String]("email")
  def password = column[String]("password")
  def role = column[Role.Role]("role")

  def * = (id.?, username,email,password,role) <> ((User.apply _).tupled, User.unapply)
}

object Users {
  var usersQuery = TableQuery[Users]
}



package models

import play.api.libs.json.Json

/**
  * Created by Suphaphong on 10/6/2016.
  */
case class ToDo(subject: String, content: String, status: String)

object ToDo {
  implicit val jsonFormat = Json.format[ToDo]
}

trait ToDoStatus {
  implicit def fromString(s: String): ToDoStatus = s.toLowerCase match {
    case "done" => Done
    case _ => Pending
  }
  implicit def toString(status: ToDoStatus): String = status.toString
}

case object Pending extends ToDoStatus
case object Done extends ToDoStatus
package controllers

import javax.inject.Inject

import models._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Result}
import services.ToDoService

/**
  * Created by Suphaphong on 10/6/2016.
  */
class ToDoController @Inject()(todoService: ToDoService) extends Controller {
  def get(subject: String) = Action {
    todoService.get(subject).fold(NotFound: Result) { todo =>
      Ok(Json.toJson(todo))
    }
  }

  def getAll = Action {
    def todoList = todoService.getAll
    Ok(Json.toJson(todoList))
  }

  def update = Action{ request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    jsonBody.map { json =>
      val todoList = (json \ "todos").as[Seq[ToDo]]
      if (todoList.nonEmpty) todoService.update(todoList)
      Ok("Process complete")
    }.getOrElse {
      BadRequest("Expecting json request body")
    }
  }

  def create = Action { request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    jsonBody.map { json =>
      val todoList = (json \ "todos").as[Seq[ToDo]]
      if (todoList.nonEmpty) todoService.save(todoList)
      Ok("Process complete")
    }.getOrElse {
      BadRequest("Expecting json request body")
    }
  }

  def remove = Action { request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    jsonBody.map { json =>
      val todoList = (json \ "todos").as[Seq[ToDo]]
      if (todoList.nonEmpty) todoService.delete(todoList.map(_.subject))
      Ok("Process complete")
    }.getOrElse {
      BadRequest("Expecting json request body")
    }
  }
}
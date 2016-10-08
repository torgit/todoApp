package controllers

import javax.inject.Inject

import models._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Result}
import services.ToDoService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Suphaphong on 10/6/2016.
  */
class ToDoController @Inject()(todoService: ToDoService)(implicit exec: ExecutionContext) extends Controller {
  def get(subject: String) = Action.async {
    todoService.get(subject).map { res =>
      Ok(Json.toJson(res))
    }
  }

  def getAll = Action.async {
    todoService.getAll.map(res => Ok(Json.toJson(res)))
  }

  def update = Action.async { request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    Future {
      jsonBody.map { json =>
        val todoList = (json \ "todos").as[Seq[ToDo]]
        if (todoList.nonEmpty) todoService.update(todoList)
        Ok("Process complete")
      }.getOrElse {
        BadRequest("Expecting json request body")
      }
    }
  }

  def create = Action.async { request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    Future {
      jsonBody.map { json =>
        val todoList = (json \ "todos").as[Seq[ToDo]]
        if (todoList.nonEmpty) todoService.save(todoList)
        Ok("Process complete")
      }.getOrElse {
        BadRequest("Expecting json request body")
      }
    }
  }

  def remove = Action.async { request =>
    val jsonBody: Option[JsValue] = request.body.asJson
    Future {
      jsonBody.map { json =>
        val todoList = (json \ "todos").as[Seq[ToDo]]
        if (todoList.nonEmpty) todoService.delete(todoList.map(_.subject))
        Ok("Process complete")
      }.getOrElse {
        BadRequest("Expecting json request body")
      }
    }
  }
}
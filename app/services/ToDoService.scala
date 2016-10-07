package services

import models._
import play.api.cache._
import com.google.inject.{ImplementedBy, Inject}

/**
  * Created by Suphaphong on 10/6/2016.
  */
@ImplementedBy(classOf[ToDoServiceImpl])
trait ToDoService {
  def getAll: Seq[ToDo]
  def get(subject: String): Option[ToDo]
  def save(todos: Seq[ToDo]): Unit
  def update(todos: Seq[ToDo])
  def delete(info: Seq[String]): Unit
}

class ToDoServiceImpl @Inject()(cache: CacheApi) extends ToDoService {

  override def getAll: Seq[ToDo] = {
    try {
      def fetchedTodo = fetchTodo()
      fetchedTodo
    }
    catch {
      case e: Exception =>
        println("Fail to fetch")
        Seq.empty
    }
  }

  override def get(subject: String): Option[ToDo] = {
    try {
      def fetchedTodo = fetchTodo(subject)
      fetchedTodo
    }
    catch {
      case e: Exception =>
        println("Fail to fetch")
        None
    }
  }

  override def save(todos: Seq[ToDo]): Unit = {
    try {
      todos.foreach(Db.save)
    }
    catch {
      case e: Exception =>
        println("Fail to save")
    }
  }

  override def delete(info: Seq[String]): Unit = {
    info.foreach { i =>
      try {
        Db.query[ToDo].whereEqual("subject", i)
          .fetchOne()
          .foreach(Db.delete[ToDo])
      }
      catch {
        case e: Exception =>
          println(e.getMessage)
          println(s"Fail to delete subject $i")
      }
    }
  }

  def fetchTodo(subject: String): Option[ToDo] = {
    Db.query[ToDo].whereEqual("subject", subject).fetch().headOption
  }

  def fetchTodo(): Seq[ToDo] = {
    Db.query[ToDo].fetch()
  }

  override def update(todos: Seq[ToDo]): Unit = {
    try {
      todos.foreach { newToDo =>
        Db.query[ToDo]
          .whereEqual("subject", newToDo.subject)
          .fetchOne()
          .map(t => t.copy(content = newToDo.content, status = newToDo.status))
          .map(Db.save)
      }
    }
    catch {
      case e: Exception =>
        println("Fail to update")
    }
  }
}

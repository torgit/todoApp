package services

import models._
import play.api.cache._
import com.google.inject.{ImplementedBy, Inject}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Suphaphong on 10/6/2016.
  */
@ImplementedBy(classOf[ToDoServiceImpl])
trait ToDoService {
  def getAll(implicit exec: ExecutionContext): Future[Seq[ToDo]]
  def get(subject: String)(implicit exec: ExecutionContext): Future[Option[ToDo]]
  def save(todos: Seq[ToDo])(implicit exec: ExecutionContext): Future[Unit]
  def update(todos: Seq[ToDo])(implicit exec: ExecutionContext): Future[Unit]
  def delete(info: Seq[String])(implicit exec: ExecutionContext): Future[Unit]
}

class ToDoServiceImpl @Inject()(cache: CacheApi) extends ToDoService {

  override def getAll(implicit exec: ExecutionContext): Future[Seq[ToDo]] = {
    try {
      def fetchedTodo = fetchTodo()
      fetchedTodo
    }
    catch {
      case e: Exception =>
        println(e.getMessage)
        println("Fail to fetch")
        Future.successful(Seq.empty)
    }
  }

  override def get(subject: String)(implicit exec: ExecutionContext): Future[Option[ToDo]] = {
    try {
      def fetchedTodo = fetchTodo(subject)
      fetchedTodo
    }
    catch {
      case e: Exception =>
        println(e.getMessage)
        println("Fail to fetch")
        Future.successful(None)
    }
  }

  override def save(todos: Seq[ToDo])(implicit exec: ExecutionContext): Future[Unit] = Future {
    try {
      todos.foreach(Db.save)
    }
    catch {
      case e: Exception =>
        println(e.getMessage)
        println("Fail to save")
    }
  }

  override def delete(info: Seq[String])(implicit exec: ExecutionContext): Future[Unit] = Future {
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

  def fetchTodo(subject: String)(implicit exec: ExecutionContext): Future[Option[ToDo]] = Future {
    Db.query[ToDo].whereEqual("subject", subject).fetch().headOption
  }

  def fetchTodo()(implicit exec: ExecutionContext): Future[Seq[ToDo]] = Future {
    Db.query[ToDo].fetch()
  }

  override def update(todos: Seq[ToDo])(implicit exec: ExecutionContext): Future[Unit] = Future {
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
        println(e.getMessage)
        println("Fail to update")
    }
  }
}

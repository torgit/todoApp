package modules

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{ToDoService, ToDoServiceImpl}

/**
  * Created by Suphaphong on 10/7/2016.
  */
class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure() = {
    bind(classOf[ToDoService]).to(classOf[ToDoServiceImpl])
  }
}

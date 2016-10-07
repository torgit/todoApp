package models

import sorm._
/**
  * Created by Suphaphong on 10/7/2016.
  */
object Db extends Instance(
  entities = Seq(Entity[ToDo](unique = Set() + Seq("subject"))),
  url = "jdbc:h2:mem:test")

package com.diveIntoScala.unit4

trait Logger {

  def log(msg: String): Unit = {
    println("msg=" + msg)
  }
}

trait DataAccess {

  def query[A](in: String): Unit = {

  }
}

trait LoggedDataAccess extends Logger with DataAccess {
  override def query[A](in: String): Unit = {
    log("query")
    super.query(in)
  }
}

object CombineTest {

  def main(args: Array[String]): Unit = {
    val logDataAccess = new LoggedDataAccess {}
    logDataAccess.query[Int]("hello")
  }
}

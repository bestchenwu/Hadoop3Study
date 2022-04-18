package com.diveIntoScala.unit5

import java.io.{File => JFile}

object FileWrapperTest {

  def main(args: Array[String]): Unit = {
    val jfile = new JFile("D:\\logs")
    val newLogTxt = jfile.\("newLog.txt")
    println(newLogTxt)
  }
}

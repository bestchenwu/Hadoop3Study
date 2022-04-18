package com.diveIntoScala

import java.io.{File => JFile}

package object unit5 {
  implicit def foo = new Foo {}

  implicit def intToString(x: Int) = "intToString:" + x.toString

  implicit def fileToFileWraper(file: JFile) = FileWrapper(file)
}




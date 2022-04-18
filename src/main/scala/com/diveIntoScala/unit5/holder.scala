package com.diveIntoScala.unit5

object holder {

  object Foo {

    trait Bar{

    }

    lazy implicit val x = new Foo {
      override def toString: String = {
        "x from holder foo"
      }
    }

    lazy implicit val bar = new Bar{
      override def toString: String = "bar from holder"
    }

    implicit val fooList = List(x)

    lazy implicit val binaryFormat = new BinaryFormat[Foo] {
      override def asBinary(entity: Foo): Array[Byte] = Array("11".toByte)
    }
  }
}

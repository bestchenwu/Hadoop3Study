package com.diveIntoScala.unit5

import com.diveIntoScala.unit5.holder.Foo


object X {
  override def toString: String = "Externally bound x object in package test"
}

class TestFooFormat(implicit format: BinaryFormat[Foo]) {
  def testFoo(implicit foo: Foo): Unit = {
    val bytes = format.asBinary(foo)
    println(bytes)
  }

}

object ImplicitlyTest {

  def findAnInt(implicit x: Int) = {
    println(s"x=${x}")
  }

  /**
   * 同一个包里的引入
   */
  def testSamePackage() = {
    //println(X)
  }

  object Wildcard {
    def X = "wildcard import x"
  }

  def testWildcardImport() = {
    //import Wildcard._
    //本地的、继承的 最高优先级
    val X = "inline x"
    println(X)
  }

  def printFoo(implicit foo: Foo) = {
    println(foo)
  }

  def main(args: Array[String]): Unit = {
    //import holder.Foo._
    //    implicit val x = 5
    //    findAnInt
    //      val foo = new Foo
    //      findAnInt(foo.x)
    //测试隐式依赖引入优先级
    //    testSamePackage()
    //    testWildcardImport()
    //    printFoo
    //implicitly函数允许在可用的作用域内寻找任何类型
    //下面这行代码是在所有的作用域内寻找List[Foo]对象
    //    val list = implicitly[List[Foo]]
    //    list.foreach(println(_))
    //
    //    val testFooFormat = new TestFooFormat()
    //    testFooFormat.testFoo
    //
    //      val bar = implicitly[Foo.Bar]
    //      println(bar)
    //从包对象里寻找到了Foo对象
    //包对象就是在unit5包下定义一个package.scala文件,里面的所有对象和类的声明加上package即可
    //printFoo //打印包对象里面的foo

    //todo:以上介绍的主要是参数的隐式转换
    //接下来介绍方法级别的隐式转换 这种也叫隐式视图
    //典型可见scala.collection.JavaConversions._
    //    import scala.collection.JavaConverters._
    //    val strings = seqAsJavaList(List("aaa"))
    //    val has = strings.contains("bbb")
    //    println(has)

    //    def printString(x: String) = println(s"x=${x}")
    //    printString(12)

    //声明隐式转换的时候不要与scala.predef里的冲突
    val x1 = 1l to 10l
    println(x1)
    import Time._
    //longWrapper方法把1l包装成了一个具备to方法(返回TimeRange)的object对象
    val x2 = 1l to 10l
    println(x2)
  }
}

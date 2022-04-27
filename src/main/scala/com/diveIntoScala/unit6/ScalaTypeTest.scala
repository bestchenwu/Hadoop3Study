package com.diveIntoScala.unit6

/**
 * scala的类型例子
 *
 * @author chenwu on 2022.4.27
 */
object Resources{

  /**
   * 用type关键字来定义结构化类型
   */
  type Resource = {
      def close():Unit
    }

    def closeResource(resource:Resource)=resource.close()
}

//object Foo{
//    type X = Int
//    def x:X = 5
//    type Y = String
//    def y:Y = "raoshanshan"
//}

object ScalaTypeTest {

  def main(args: Array[String]): Unit = {
    //Resources.closeResource(System.in)
    //testX(foo)
//    val foo:T = new {
//      type X = Int
//      def x:X = 5
//      type Y = String
//      def y:Y = "raoshanshan"
//    }
////    val x = returnX(foo)
////    println(x)
//      testX(foo)

      val u = testFooU(Foo.baz.bar)
      println("u="+u)
  }
}

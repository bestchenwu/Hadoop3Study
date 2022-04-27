package com.diveIntoScala

package object unit6 {

    type T = {
        type X = Int
        def x:X
        type Y
        def y:Y
    }

    def testX(t:T) = {
        println("testX:"+t.x)
    }

    //这里# 表示引入嵌套的类型
    //具体在这个例子里表示引用T类型的X类型
    def returnX(t:T): T#X = t.x

    object Foo{
        type T = {
            type U
            def bar:U
        }
        //用val 可以确保在整个生命周期都不会被改变,所以是一个稳定的引用
        val baz : T = new {
            type U = String
            def bar:U = "lisisi"
        }
    }

    def testFooU(u:Foo.baz.U) = u
}

package com.flinkTheory.unit5;

import java.lang.reflect.Field;

class Outer1{

    public class FirstInner {

    }
}

public class This0Test {

    public static void main(String[] args)  throws Exception{
        Outer1 outer1 = new Outer1();
        Outer1.FirstInner firstInner = outer1.new FirstInner();
        System.out.println("outer1 hashCode:"+outer1.hashCode());
        System.out.println("first inner hashCode:"+firstInner.hashCode());

        Field this$0 = firstInner.getClass().getDeclaredField("this$0");
        this$0.setAccessible(true);
        Object output11 = this$0.get(firstInner);
        //这里显示了output11恰恰就是外部对象outer1
        System.out.println("output11 hashCode:"+output11.hashCode());
    }
}

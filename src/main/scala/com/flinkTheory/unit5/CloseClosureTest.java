package com.flinkTheory.unit5;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.java.ClosureCleaner;

import java.io.Serializable;

class Outer {
    public class Inner implements Serializable {

    }
}

public class CloseClosureTest {

    public static void main(String[] args) {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        //详细可见https://www.cnblogs.com/qiumingcheng/p/9667426.html
        //clean的作用是将外部引用对象置为null 避免外部对象没有实现serializable接口导致序列化失败
        ClosureCleaner.clean(inner, ExecutionConfig.ClosureCleanerLevel.RECURSIVE
                , false);
    }
}

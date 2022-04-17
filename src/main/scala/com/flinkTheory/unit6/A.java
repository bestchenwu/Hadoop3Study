package com.flinkTheory.unit6;

public class A {

    private long a;

    public A() {
        this.a = 1;
    }

    public void printA() {
        System.out.println("print a=" + a);
    }
}

class Guard{
    private int ACCESS_ALLOWED = 1;

    public boolean isAllowAccessed(){
        return 42 == ACCESS_ALLOWED;
    }

    @Override
    public String toString() {
        return "access_allowed="+ACCESS_ALLOWED;
    }
}
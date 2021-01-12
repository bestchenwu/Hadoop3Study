package java8Study.unit9;

/**
 * 多类继承的测试
 *
 * @author chenwu on 2021.1.12
 */
interface A {

    default void hello() {
        System.out.println("hello from A");
    }
}

interface B extends A {

    @Override
    default void hello() {
        System.out.println("hello from B");
    }
}

class D implements A{

}

class C extends  D implements B, A {

    public void sayHello() {
        //这里还是输出hello from B,因为B提供了更具体的实现
        this.hello();
    }
}

public class MultiExtendTest {

    public static void main(String[] args) {
        C c = new C();
        c.sayHello();
    }
}

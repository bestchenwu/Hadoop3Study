package java8Study.unit3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Consumer;

public class PredicateTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Predicate<String> p = (s->list.add(s));
        //尽管Consumer要求函数返回值是void,但是对于boolean等返回值也可以兼容
        Consumer<String> b = s->list.add(s);
    }
}

package java8Study.unit3;

import java.util.function.Function;

/**
 * 函数的复合/组合
 *
 * @author chenwu on 2020.12.24
 */
public class ComposeThen {

    public static void main(String[] args) {
        Function<Integer,Integer> f = (Integer x)->x+1;
        Function<Integer,Integer> g = (Integer x)->x*2;
        Function<Integer,Integer> h = f.andThen(g);
        Integer result = h.apply(5);
        System.out.println(result);
    }
}

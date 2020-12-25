package java8Study.unit4;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 无限流的测试类
 *
 * @author chenwu on 2020.12.25
 */
public class InifiniteSteamTest {

    public static void main(String[] args) {
        //生成前5个偶数
        //IntStream.iterate(0,n->n+2).limit(5).forEach(System.out::println);
        //IntStream.generate(()->10).limit(5).forEach(System.out::println);
        Stream<int[]> iterateStream = Stream.iterate(new int[]{0, 1}, intArray -> {
            return new int[]{intArray[1], intArray[0]+intArray[1]};
        });
        iterateStream.map(intArray->intArray[0]).limit(5).forEach(System.out::println);
    }
}

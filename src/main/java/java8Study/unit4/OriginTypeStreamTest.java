package java8Study.unit4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java8Study.unit4.Dish.Type;

/**
 * 原始类型流,例如{@link java.util.stream.IntStream}等
 *
 * @author chenwu on 2020.12.25
 */
public class OriginTypeStreamTest {

    public static void main(String[] args) {
//        List<Dish> dishList = Arrays.asList(new Dish("dish1", true, 18, Type.FISH), new Dish("dish2", false, 21, Type.MEAT));
//        //将对象流转换为原始类型流
//        IntStream intStream = dishList.stream().mapToInt(Dish::getCalories);
//        int totalCalories = intStream.sum();
//        System.out.println("totalCalories=" + totalCalories);
        //将原始类型流转换为对象流
        //Stream<Integer> integerStream = intStream.boxed();
        //生成勾股数
        //这里要将intStream转换为对象流的原因 是intStream flatMap只能转换为IntStream
        //转换为对象流后 flatMap就可以将Stream<Stream<T>> 转换为Stream<T>
        //Math.sqrt(a * a + b * b) % 1 == 0  用来判断a2+b2 是否可以完全开方
//        Stream<int[]> stream = IntStream.range(1, 100).boxed().flatMap(a ->
//                IntStream.range(a, 100).filter(b -> Math.sqrt(a * a + b * b) % 1 == 0).mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
//        );
        //优化代码
//        Stream<double[]> stream = IntStream.range(1, 100).boxed().flatMap(a -> IntStream.range(a, 100).mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)}).filter(t -> t[2] % 1 == 0));
//        stream.limit(5).forEach(intArray -> System.out.println(Arrays.toString(intArray)));
        //通过of表达式创建值流
        Stream.of("java 8", "lambda", "in").map(String::toUpperCase).forEach(System.out::println);
    }
}

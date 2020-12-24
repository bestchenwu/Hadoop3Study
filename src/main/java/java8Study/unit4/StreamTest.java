package java8Study.unit4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流式测试类
 *
 * @author chenwu on 2020.12.24
 */
public class StreamTest {

    public static void main(String[] args) {
        //将字符串里不重复字符组成字符串列表返回
        //List<String> list = Arrays.asList("java8", "lambda", "action");
        //List<String> result = list.stream().map(item -> item.split("")).flatMap(strArray -> Arrays.stream(strArray))
        //        .distinct().collect(Collectors.toList());
        //可以简化为:
//        List<String> result = list.stream().map(item -> item.split("")).flatMap(Arrays::stream)
//                .distinct().collect(Collectors.toList());
//        List<Integer> list1 = Arrays.asList(1, 2, 3);
//        List<Integer> list2 = Arrays.asList(3, 4);
//        //flatMap表示将各个生成流扁平化为一个流
//        List<int[]> result = list1.stream().flatMap(item1 -> list2.stream().map(item2 -> new int[]{item1, item2})).collect(Collectors.toList());
//        result.forEach(item-> System.out.println(Arrays.toString(item)));
        //System.out.println(result);
        //找出第一个平方能被3整除的数
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
//        Integer result = someNumbers.stream().filter(i -> (i * i) % 3 == 0).findAny().orElse(-1);
//        System.out.println(result);
        //求列表的元素和
        //Integer result = someNumbers.stream().reduce(Integer::sum).orElse(-1);
        //求列表的最大值/最小值
        Integer result = someNumbers.stream().reduce(Integer::max).orElse(-1);
        System.out.println(result);
    }
}

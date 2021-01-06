package java8Study.unit6;

import java8Study.unit4.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * 多级分组/分片
 *
 * @author chenwu on 2021.1.5
 */
public class MultiCollectorTest {

    public static void main(String[] args) {
        List<Dish> dishList = Arrays.asList(new Dish("dish1", false, 18, Dish.Type.FISH), new Dish("dish2", true, 31, Dish.Type.MEAT), new Dish("dish3", true, 9, Dish.Type.OTHER));
//        Map<String, Map<String, List<Dish>>> map = dishList.stream().collect(groupingBy(Dish::getName, groupingBy(dish -> {
//            if (dish.isVegetarian()) {
//                return "vegetable";
//            } else {
//                return "meat";
//            }
//        })));
        Map<Boolean, List<Dish>> map = dishList.stream().collect(partitioningBy(Dish::isVegetarian));
        System.out.println(map);
//        System.out.println(map);
    }
}

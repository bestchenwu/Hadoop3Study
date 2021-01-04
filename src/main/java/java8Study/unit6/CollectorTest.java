package java8Study.unit6;

import java8Study.unit4.Dish;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class CollectorTest {

    public static void main(String[] args) {
        List<Dish> list = Arrays.asList(new Dish("dish1", false, 18, Dish.Type.FISH), new Dish("dish2", true, 31, Dish.Type.MEAT), new Dish("dish3", true, 9, Dish.Type.OTHER));
//        Long count = list.stream().collect(Collectors.counting()); //等价于list.stream().count()
//        System.out.println("count=" + count);
        //获取流中能量最高的元素
//        Comparator<Dish> dishComparator = Comparator.comparing(Dish::getCalories);
//        Optional<Dish> maxDish = list.stream().collect(maxBy(dishComparator));
//        if(maxDish.isPresent()){
//            Dish dish = maxDish.get();
//            System.out.println(dish);
//        }

        //获取能量总和
//        Integer totalCalories = list.stream().collect(summingInt(Dish::getCalories));
//        System.out.println(totalCalories);
        //获取能量最大的Dish
//        Optional<Dish> optionalDish = list.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
//        if(optionalDish.isPresent()){
//            System.out.println("max calory dish:"+optionalDish.get());
//        }
        //分组
        Map<String, List<Dish>> collectMap
                = list.stream().collect(groupingBy(Dish::getName));
        System.out.println(collectMap);
    }
}

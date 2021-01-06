package java8Study.unit6;

import java.util.Arrays;
import java.util.List;

public class ToListCollectorTest {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 5, 2, 4, 8, 9, 6);
        List<Integer> result = list.stream().collect(new ToListCollector<>());
        System.out.println("result=" + result);
    }
}

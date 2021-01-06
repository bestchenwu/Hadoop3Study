package java8Study.unit6;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * 测试是否是质数
 *
 * @author chenwu on 2021.1.6
 */
public class IsPrimeTest {

    private static boolean isPrime(int n) {
        if (n <= 3) {
            return true;
        }
        int sqrt = (int) Math.ceil(Math.sqrt(n));
        boolean isPrime = IntStream.rangeClosed(2, sqrt).noneMatch(i -> {
            return n % i == 0;
        });
        return isPrime;
    }

    public static void main(String[] args) {
        IsPrimeTest isPrimeTest = new IsPrimeTest();
//        System.out.println(isPrimeTest.isPrime(9));
//        System.out.println(isPrimeTest.isPrime(14));
//        System.out.println(isPrimeTest.isPrime(17));
        int n = 29;
//        Map<Boolean, List<Integer>> isPrimeMap = IntStream.rangeClosed(1, n).boxed().collect(partitioningBy(i -> isPrime(i)));
        //使用自定义的质数归约器
        Map<Boolean, List<Integer>> isPrimeMap = IntStream.rangeClosed(2, n).boxed().collect(new IsPrimeCollector());
        System.out.println(isPrimeMap);
    }
}

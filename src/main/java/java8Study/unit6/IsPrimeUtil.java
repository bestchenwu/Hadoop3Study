package java8Study.unit6;

import java.util.List;
import java.util.function.Predicate;

/**
 * 质数工具类
 *
 * @author chenwu on 2021.1.6
 */
public class IsPrimeUtil {

    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }

    public static boolean isPrime(List<Integer> primes, int candidate) {
        if(candidate ==1){
            return true;
        }
        int sqrt = (int) Math.sqrt(candidate);
        return takeWhile(primes, i -> i <= sqrt).stream().noneMatch(i -> candidate % i == 0);
    }
}

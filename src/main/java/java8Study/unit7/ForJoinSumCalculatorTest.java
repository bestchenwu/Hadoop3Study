package java8Study.unit7;

import java.util.stream.LongStream;

/**
 * {@link ForJoinSumCalculator}的单元测试类
 *
 * @author chenwu on 2021.1.8
 */
public class ForJoinSumCalculatorTest {

    public static void main(String[] args) {
        long n = 100_00*10_000;
        long[] numbers = LongStream.rangeClosed(1,n).toArray();
        ForJoinSumCalculator task = new ForJoinSumCalculator(numbers);
        Long result = task.invoke();
        System.out.println("result="+result);
    }
}

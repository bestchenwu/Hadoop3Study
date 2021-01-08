package java8Study.unit7;

import java.util.stream.LongStream;

public class ParallelTest {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Long sum = LongStream.rangeClosed(1, 10000 * 100).parallel().reduce(0l, Long::sum);
        long endTime = System.currentTimeMillis();
        System.out.println("parallel time :"+(endTime-startTime));
        startTime = System.currentTimeMillis();
        LongStream.rangeClosed(1, 10000 * 100).reduce(0l, Long::sum);
        endTime = System.currentTimeMillis();
        System.out.println("not parallel time:"+(endTime-startTime));
    }
}

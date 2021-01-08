package java8Study.unit7;

import java.util.concurrent.RecursiveTask;

/**
 * fork/join task框架体系的解释  https://www.cnblogs.com/takemybreathaway/articles/9762938.html
 *
 * @author chenwu on 2021.1.8
 */
public class ForJoinSumCalculator extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;

    private static final long THRESH_HOLD = 10_000;

    public ForJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length < THRESH_HOLD) {
            return computeBySequence();
        }
        ForJoinSumCalculator leftTask = new ForJoinSumCalculator(numbers, start, (start + end) / 2);
        ForJoinSumCalculator rightTask = new ForJoinSumCalculator(numbers, (start + end) / 2, end);
        //执行子任务
        leftTask.fork();
        rightTask.fork();
        //等待子任务执行完成，并得到结果
        Long rightResult = leftTask.join();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeBySequence() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }
}

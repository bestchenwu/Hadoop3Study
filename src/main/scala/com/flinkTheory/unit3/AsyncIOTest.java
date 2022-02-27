package com.flinkTheory.unit3;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;


import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * flink异步交互测试<br/>
 * 详细可见https://www.jianshu.com/p/cefc245e51c7
 *
 * @author chenwu on 2022.2.27
 */
public class AsyncIOTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        int sum = 5;
        final long timeout = 40*100;
        DataStreamSource<Integer> inputStream = env.addSource(new SimpleFunction(sum));
        AsyncFunction<Integer,String> function = new SimpleSyncFunction();
        SingleOutputStreamOperator<String> result = AsyncDataStream.unorderedWait(inputStream, function, timeout, TimeUnit.MILLISECONDS, 10);
        SingleOutputStreamOperator<String> finalResult = result.map(new MapFunction<String, String>() {

            @Override
            public String map(String value) throws Exception {
                return value + "," + System.currentTimeMillis();
            }
        });
        finalResult.print();
        env.execute("AsyncIOTest");
    }


}

 class SimpleFunction implements SourceFunction<Integer>{

    private static volatile boolean isRunning = true;
    private int counter = 0 ;
    private int start = 0;

    public SimpleFunction(int counter){
        this.counter = counter;
    }

    @Override
    public void run(SourceContext<Integer> ctx) throws Exception {
        while(start<=counter && isRunning){
            synchronized (ctx.getCheckpointLock()){
                System.out.println("send data:"+start);
                ctx.collect(start);
                start++;
            }
            Thread.sleep(10l);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}

 class SimpleSyncFunction extends RichAsyncFunction<Integer,String> {

    private long[] sleepArray  = new long[]{100,200,300,400,500};

    @Override
    public void asyncInvoke(Integer input, ResultFuture<String> resultFuture) {
        System.out.println(System.currentTimeMillis()+"-input:"+input+" will sleep "+sleepArray[input]);
    }

    private void query(final Integer input,final ResultFuture<String> resultFuture){
        try{
            Thread.sleep(sleepArray[input]);
            resultFuture.complete(Collections.singletonList(String.valueOf(input)));
        }catch (InterruptedException e){
            resultFuture.complete(Collections.singletonList(String.valueOf(0)));
        }
    }

    private void asyncQuery(final Integer input,final ResultFuture<String> resultFuture){
        CompletableFuture.supplyAsync(new Supplier<Integer>() {

            @Override
            public Integer get() {
                try{
                    Thread.sleep(sleepArray[input]);
                    return input;
                }catch(InterruptedException e){
                    return 0;
                }
            }
        }).thenAccept((Integer result)->resultFuture.complete(Collections.singletonList(String.valueOf(input))));
    }
}

package com.flinkTheory.unit3;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.scala.async.ResultFuture;
import org.apache.flink.streaming.api.scala.async.RichAsyncFunction;

public class AsyncIOTest {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        int sum = 5;
        DataStreamSource<Integer> source = env.addSource(new SimpleFunction(sum));

    }

    private static class SimpleFunction implements SourceFunction<Integer>{

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

    public class SimpleSyncFunction extends RichAsyncFunction<Integer,String>{

        private long[] sleepArray  = new long[]{100,200,300,400,500};

        @Override
        public void asyncInvoke(Integer input, ResultFuture<String> resultFuture) {

        }
    }
}

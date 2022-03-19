package com.flinkTheory.unit3;

import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.Arrays;
import java.util.List;

/**
 * 实现ListCheckPoint的数据源
 */
public class CounterSource extends RichParallelSourceFunction<Long> implements ListCheckpointed<Long> {

    private long counter = 0;
    private volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<Long> sourceContext) throws Exception {
        while(isRunning){
            synchronized (sourceContext.getCheckpointLock()){
                sourceContext.collect(counter);
                counter++;
            }
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }

    @Override
    public List<Long> snapshotState(long checkPointedId, long checkPointedTimeStamp) throws Exception {
        return Arrays.asList(counter);
    }

    @Override
    public void restoreState(List<Long> list) throws Exception {
        for(Long item:list){
            counter = item;
        }
    }
}

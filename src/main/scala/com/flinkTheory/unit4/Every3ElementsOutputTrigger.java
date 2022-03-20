package com.flinkTheory.unit4;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 平均每3个元素就输出一次
 *
 * @author chenwu on 2022.3.20
 */
public class Every3ElementsOutputTrigger extends Trigger<Object, TimeWindow> {

    private int flag = 0;

    @Override
    public TriggerResult onElement(Object element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        if(flag>2){
            flag = 0;
            return TriggerResult.FIRE;
        }else{
            flag++;
        }
        System.out.println("on element:"+element);
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        ctx.deleteProcessingTimeTimer(window.maxTimestamp());
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void onMerge(TimeWindow window, OnMergeContext ctx) throws Exception {
        long windowMaxTimeStamp = window.maxTimestamp();
        ctx.registerProcessingTimeTimer(windowMaxTimeStamp);
    }
}

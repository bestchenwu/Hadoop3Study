package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TemperatureReducer extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable> {

    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int max_value = Integer.MIN_VALUE;
        for(IntWritable value : values){
            max_value = Math.max(max_value,value.get());
        }
        context.write(key,new IntWritable(max_value));
    }
}

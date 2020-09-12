package hadoopStudy.unit2_map_reduce_init.reduce;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class TemperatureReducer extends Reducer<IntWritable, FloatWritable, IntWritable,Text> {

    @Override
    protected void reduce(IntWritable key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        Float maxValue = Float.MIN_VALUE;
        for(FloatWritable value:values){
            maxValue = Math.max(value.get(),maxValue);
        }
        //todo:使用java的流式处理，则生成的最大值不准
        //FloatWritable maxTemperature = StreamSupport.stream(values.spliterator(), false).max((f1, f2) -> Float.compare(f1.get(),f2.get())).get();
        //这种方式 所有数据都写在一个文件里
//        String output = key.get()+ SymbolConstants.SYMBOL_DH+maxValue;
//        context.write(NullWritable.get(),new Text(output));
        //输出的时候 有规律的把key和value按tab隔开
        /*
         *1991	35.1
        1992	38.3
        1993	74.2
        1994	47.8
        1997	32.1
         *
         */
        context.write(key,new Text(maxValue.toString()));
    }
}

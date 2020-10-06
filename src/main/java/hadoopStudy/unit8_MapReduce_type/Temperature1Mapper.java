package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 处理/hadoop/temperature1.txt<br/>
 * 文本内容如下所示:
 * 2020,11
 * 2021,12
 * 2020,14
 * 2021,9
 * 2022,8
 * 2022,14
 *
 * @author chenwu on 2020.10.6
 */
public class Temperature1Mapper extends Mapper<LongWritable, Text, IntWritable,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] splitArray = str.split(SymbolConstants.SYMBOL_DH);
        IntWritable writeKey = new IntWritable(Integer.parseInt(splitArray[0]));
        IntWritable writeValue = new IntWritable(Integer.parseInt(splitArray[1]));
        context.write(writeKey,writeValue);
    }
}

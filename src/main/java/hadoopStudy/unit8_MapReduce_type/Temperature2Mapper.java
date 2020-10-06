package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 处理内容/hadoop/temperature2.txt<br/>
 * 2020-21
 * 2020-3
 * 2021-4
 * 2021-8
 * 2022-18
 * 2022-2
 *
 * @author chenwu on 2020.10.6
 */
public class Temperature2Mapper extends Mapper<LongWritable, Text, IntWritable,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String textValue = value.toString();
        String[] splitArray = textValue.split(SymbolConstants.SYMBOL_DHG);
        IntWritable writeKey = new IntWritable(Integer.parseInt(splitArray[0]));
        IntWritable writeValue = new IntWritable(Integer.parseInt(splitArray[1]));
        context.write(writeKey,writeValue);
    }
}

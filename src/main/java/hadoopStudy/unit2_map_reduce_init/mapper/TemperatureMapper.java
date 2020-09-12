package hadoopStudy.unit2_map_reduce_init.mapper;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TemperatureMapper extends Mapper<LongWritable, Text, IntWritable,FloatWritable> {

    /**
     * 键key是内容在文件中的行偏移量
     * value才是真正的值
     *
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String content = value.toString();
        String[] splitArray = content.split(SymbolConstants.SYMBOL_DH);
        int year = Integer.parseInt(splitArray[0]);
        float temperature = Float.parseFloat(splitArray[1]);
        context.write(new IntWritable(year),new FloatWritable(temperature));
    }

}

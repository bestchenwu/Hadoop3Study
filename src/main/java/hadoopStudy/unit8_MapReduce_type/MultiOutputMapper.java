package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MultiOutputMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] splitArray = str.split(SymbolConstants.SYMBOL_DH);
        Text writeKey = new Text(splitArray[0]);
        IntWritable writeValue = new IntWritable(Integer.parseInt(splitArray[1]));
        context.write(writeKey,writeValue);
    }
}

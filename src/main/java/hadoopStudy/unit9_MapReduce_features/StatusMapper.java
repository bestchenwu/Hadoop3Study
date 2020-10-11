package hadoopStudy.unit9_MapReduce_features;

import common.constants.SymbolConstants;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class StatusMapper extends Mapper<LongWritable, Text, IntWritable,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] splitArray = str.split(SymbolConstants.SYMBOL_DH);
        Integer year = Integer.parseInt(splitArray[0]);
        IntWritable temperature = new IntWritable(0);
        if(!NumberUtils.isNumber(splitArray[1])){
            context.getCounter(StatusEnum.UNVALID_NUMBER).increment(1l);
        }else{
            int number = Integer.parseInt(splitArray[1]);
            if(number<=0){
                context.getCounter(StatusEnum.NEGETIVE_NUMBER).increment(1l);
            }else{
                temperature.set(number);
            }
        }
        context.write(new IntWritable(year),temperature);
    }
}

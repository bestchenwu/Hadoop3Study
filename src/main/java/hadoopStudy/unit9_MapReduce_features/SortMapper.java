package hadoopStudy.unit9_MapReduce_features;

import common.constants.SymbolConstants;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text,IntPair, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        String[] splitArray = str.split(SymbolConstants.SYMBOL_DH);
        IntPair writeKey = new IntPair(Integer.parseInt(splitArray[0]),Integer.parseInt(splitArray[1]));
        context.write(writeKey,NullWritable.get());
    }
}

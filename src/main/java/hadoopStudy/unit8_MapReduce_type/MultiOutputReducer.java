package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

/**
 * 多输出任务
 *
 * @author chenwu on 2020.10.6
 */
public class MultiOutputReducer extends Reducer<Text, IntWritable,NullWritable,IntWritable> {

    private MultipleOutputs<NullWritable,IntWritable> multipleOutputs;
    private String baseOutputPath = "/hadoop/multi/%s";

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        multipleOutputs = new MultipleOutputs<NullWritable, IntWritable>(context);
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //最终会按照名称输出如下的文件
        //文件名的形式是name-m-nnnnn形式 name是由用户指定的key,nnnnn是指明块号的整数(从00000开始)
        ///hadoop/multi/beijing-r-00000 里面的内容就是北京站下的所有气温
        ///hadoop/multi/shanghai-r-00000
        for(IntWritable value: values){
            String outputPath = String.format(baseOutputPath,key.toString());
            multipleOutputs.write(NullWritable.get(),value,outputPath);
        }
    }
}

package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 测试多个输入
 *
 * @author chenwu on 2020.10.6
 */
public class MultiInputJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"MultiInputJob");
        job.setJarByClass(getClass());
        String[] splitArray = args[0].split(SymbolConstants.SYMBOL_DH);
        MultipleInputs.addInputPath(job,new Path(splitArray[0]), TextInputFormat.class,Temperature1Mapper.class);
        MultipleInputs.addInputPath(job,new Path(splitArray[1]), TextInputFormat.class,Temperature2Mapper.class);
        Path outputPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(outputPath.toUri(),conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        FileOutputFormat.setOutputPath(job,outputPath);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        job.setReducerClass(TemperatureReducer.class);
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        MultiInputJob multiInputJob = new MultiInputJob();
        int result = ToolRunner.run(multiInputJob, args);
        System.exit(result);
    }
}

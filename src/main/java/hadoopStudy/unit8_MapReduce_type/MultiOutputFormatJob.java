package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 测试多输出的job
 *
 * @author chenwu on 2020.10.6
 */
public class MultiOutputFormatJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"MultiOutputFormatJob");
        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(outputPath.toUri(),conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        FileInputFormat.addInputPath(job,inputPath);
        FileOutputFormat.setOutputPath(job,outputPath);
        job.setJarByClass(getClass());
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapperClass(MultiOutputMapper.class);
        job.setReducerClass(MultiOutputReducer.class);
        return job.waitForCompletion(true)?0:-1;
    }

    public static void main(String[] args) throws Exception {
        MultiOutputFormatJob job = new MultiOutputFormatJob();
        int result = ToolRunner.run(job, args);
        System.exit(result);
    }
}

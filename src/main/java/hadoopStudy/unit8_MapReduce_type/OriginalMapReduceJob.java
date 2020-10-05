package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 原始的输入输出作业
 *
 * @author chenwu on 2020.10.2
 */
public class OriginalMapReduceJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Job job = Job.getInstance(conf, "OriginalMapReduceJob");
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outputPath = new Path(args[1]);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        FileOutputFormat.setOutputPath(job, outputPath);
        //默认的mapper 输入key是行偏移量,value是值,输出也是原封不动的输出(key,value)
        boolean result = job.waitForCompletion(true);
        return result ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        OriginalMapReduceJob job = new OriginalMapReduceJob();
        int result = ToolRunner.run(job, args);
        System.exit(result);
    }
}

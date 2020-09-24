package hadoopStudy.unit2_map_reduce_init;

import hadoopStudy.unit2_map_reduce_init.mapper.TemperatureMapper;
import hadoopStudy.unit2_map_reduce_init.reduce.TemperatureReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 从文件中按自然年份获取最大的温度
 *
 * @author chenwu on 2020.9.5
 */
public class MaxTemperatureJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        //指定队列名称为hadoop
        conf.set("mapred.job.queue.name","hadoop");
        Path inputPutPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);
        FileSystem fileSystem = FileSystem.get(conf);
        if (fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath, true);
        }
        Job job = Job.getInstance(conf);
        FileInputFormat.setInputPaths(job, inputPutPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        //设置输出为GZIP压缩
        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
        job.setJarByClass(getClass());
        job.setMapperClass(TemperatureMapper.class);
        //当mapper的输出和reducer的输出类型一致的时候 不需要设置key value class
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(FloatWritable.class);
        job.setReducerClass(TemperatureReducer.class);
        boolean result = job.waitForCompletion(true);
        return result ? 1 : 0;
    }

    public static void main(String[] args) throws Exception {
        MaxTemperatureJob job = new MaxTemperatureJob();
        int code = ToolRunner.run(job, args);
        System.exit(code);
    }
}

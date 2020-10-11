package hadoopStudy.unit9_MapReduce_features;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 通过对键进行分组和排序
 *
 * @author chenwu on 2020.10.11
 */
public class SortJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(outputPath.toUri(), conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        Job job = Job.getInstance(conf, "SortJob");
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        job.setJarByClass(getClass());
        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(IntPair.class);
        job.setOutputValueClass(NullWritable.class);
        //这样生成了一个全局有序的文件(key按升序排序,value降序排序并取第一个值)
        //根据key进行分区,确保同一个key进入到同一个分区
        job.setPartitionerClass(SortPartitioner.class);
        //在map阶段的末尾执行排序,按照key升序,value降序的方式，这样同样年份的数据第一条数据最大
        job.setGroupingComparatorClass(SortGroupComparator.class);
        //在reduce阶段的归并操作之前进行
        //确保同一个年份的进入同一个分组
        //这样[1990,10] [1990,11]会进入同一个分组
        //而reduce阶段相同的key只取第一个,也就变相的达到了按年份取最大值的效果
        job.setSortComparatorClass(SortKeyComparator.class);
        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        SortJob sortJob = new SortJob();
        int result = ToolRunner.run(sortJob, args);
        System.exit(result);
    }
}

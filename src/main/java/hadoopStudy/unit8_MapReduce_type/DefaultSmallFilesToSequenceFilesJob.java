package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 与{@link SmallFilesToSequenceFilesJob}进行比较,查看mapper的个数是否受{@link WholeFileInputFormat}的影响
 *
 * @author chenwu on 2020.10.6
 */
public class DefaultSmallFilesToSequenceFilesJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String inputFiles = args[0];
        String output = args[1];
        Job job = Job.getInstance(conf, DefaultSmallFilesToSequenceFilesJob.class.getSimpleName());
        Path inputPath = new Path(inputFiles);
        Path outputPath = new Path(output);
        FileSystem fs = FileSystem.get(outputPath.toUri(),conf);
        //对输入路径循环累加
        FileStatus fileStatus = fs.getFileStatus(inputPath);
        if(fileStatus.isDirectory()){
            FileStatus[] fileStatuses = fs.listStatus(inputPath);
            for(FileStatus file : fileStatuses){
                FileInputFormat.addInputPath(job,file.getPath());
            }
        }else{
            FileInputFormat.addInputPath(job,inputPath);
        }
        //如果输出路径存在，则先递归删除
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        SequenceFileOutputFormat.setOutputPath(job,outputPath);
        job.setJarByClass(getClass());
        //Mapper的个数由getSplits方法返回的List<InputSplit>的大小确定
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        DefaultSmallFilesToSequenceFilesJob job = new DefaultSmallFilesToSequenceFilesJob();
        int result = ToolRunner.run(job, args);
        System.exit(result);
    }
}

package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import hadoopStudy.unit5_compress.SmallFilesToSequenceFilesMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * 将若干小文件合并成顺序文件
 *
 * @author chenwu on 2020.10.5
 */
public class SmallFilesToSequenceFilesJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String inputFiles = args[0];
        String output = args[1];
        Job job = Job.getInstance(conf, SmallFilesToSequenceFilesJob.class.getSimpleName());
        for(String file : inputFiles.split(SymbolConstants.SYMBOL_DH)){
            WholeFileInputFormat.addInputPath(job,new Path(file));
        }
        Path outputPath = new Path(output);
        FileSystem fs = FileSystem.get(outputPath.toUri(),conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        job.setJarByClass(getClass());
        job.setMapperClass(SmallFilesToSequenceFilesMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setInputFormatClass(WholeFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        SmallFilesToSequenceFilesJob job = new SmallFilesToSequenceFilesJob();
        int result = ToolRunner.run(job, args);
        System.exit(result);
    }
}

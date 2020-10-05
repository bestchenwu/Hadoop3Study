package hadoopStudy.unit8_MapReduce_type;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 将整个文件作为输入源,不进行分片
 *
 * @author chenwu on 2020.10.5
 */
public class WholeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        WholeFileReader wholeFileReader = new WholeFileReader();
        wholeFileReader.initialize(split,context);
        return wholeFileReader;
    }
}

class WholeFileReader extends RecordReader<NullWritable,BytesWritable>{

    private FileSplit fileSplit;
    private Configuration conf;
    private BytesWritable value = new BytesWritable();
    private boolean isProcessed = false;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit)split;
        this.conf = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(!isProcessed){
            FileSystem fs = null;
            FSDataInputStream in = null;
            try{
                byte[] contents = new byte[(int)fileSplit.getLength()];
                Path path = fileSplit.getPath();
                in = fs.open(path);
                IOUtils.readFully(in,contents,0,contents.length);
                value.set(contents,0,contents.length);
                fs = FileSystem.get(conf);
            }catch(IOException e){
                throw e;
            }finally {
                IOUtils.closeStream(in);
                if(fs!=null){
                    fs.close();
                }

            }
            isProcessed = true;
            return true;
        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return isProcessed?0.0f:1.0f;
    }

    @Override
    public void close() throws IOException {

    }
}

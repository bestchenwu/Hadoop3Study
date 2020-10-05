package hadoopStudy.unit5_compress;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 将多个小文件合并成顺序文件
 *
 * @author chenwu on 2020.10.5
 */
public class SmallFilesToSequenceFilesMapper extends Mapper<NullWritable, BytesWritable, Text,BytesWritable> {

    private Text fileNameKey;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        Path path = fileSplit.getPath();
        fileNameKey = new Text(path.getName());
    }

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        context.write(fileNameKey,value);
    }
}

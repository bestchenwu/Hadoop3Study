package hadoopStudy.unit5_compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 适合顺序写的日志文件
 */
public class SequenceFileWriterTest {

    private static String[] data = new String[]{"first","second","third"};

    public static void main(String[] args) {
        String uri = "hdfs://master:9000/hadoop/sequence.txt";
        Configuration conf = new Configuration();
        FileSystem fs = null;
        Writer writer = null;
        try {
            fs = FileSystem.get(new URI(uri),conf);
            Path path = new Path(uri);
            writer = SequenceFile.createWriter(conf, Writer.file(path),Writer.keyClass(IntWritable.class),Writer.valueClass(Text.class),Writer.compression(SequenceFile.CompressionType.NONE));
            IntWritable key = new IntWritable();
            Text value = new Text();
            for(int i = 0;i<100;i++){
                key.set(100-i);
                value.set(data[i%data.length]);
                writer.append(key,value);
            }
            writer.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}

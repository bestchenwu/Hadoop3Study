package hadoopStudy.unit5_compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 顺序读文件
 */
public class SequenceFileReaderTest {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        String uri = "hdfs://master:9000/hadoop/sequence.txt";
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(new URI(uri),conf);
            Reader reader = new SequenceFile.Reader(conf,Reader.file(new Path(uri)));
            IntWritable key = (IntWritable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Text value = (Text)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            //顺序文件的同步点的位置信息
            long position = reader.getPosition();
            while(reader.next(key,value)){
                System.out.printf("%d\t%s\t%s\n",position,key.get(),value.toString());
                position = reader.getPosition();
            }
            reader.close();
            fileSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

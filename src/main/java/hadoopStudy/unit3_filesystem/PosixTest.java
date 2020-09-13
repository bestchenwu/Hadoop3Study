package hadoopStudy.unit3_filesystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 文件创建和写入一致性测试
 *
 * @author chenwu on 2020.9.13
 */
public class PosixTest {

    String newFile = "hdfs://127.0.0.1:9000/hadoop/newFile";

    //@Test
    public void testCreate(){
        try{
            FileSystem fs = FileSystem.get(new URI(newFile),new Configuration());
            Path path = new Path(newFile);
            fs.create(path);
            Assert.assertEquals(true,fs.exists(path));
        }catch(URISyntaxException | IOException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWrite(){
        OutputStream os = null;
        try{
            FileSystem fs = FileSystem.get(new URI(newFile),new Configuration());
            Path path = new Path(newFile);
            os = fs.create(path,true);
            os.write("content".getBytes());
            //hflush调用成功后，保证到达所有的datanode的写入管道，并对所有的新reader可见
            ((FSDataOutputStream) os).hflush();
            Assert.assertEquals(7l,fs.getFileStatus(path).getLen());
        }catch(URISyntaxException | IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeStream(os);
        }

    }
}

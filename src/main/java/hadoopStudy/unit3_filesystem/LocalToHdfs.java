package hadoopStudy.unit3_filesystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 将本地文件拷贝到hdfs
 *
 * @author chenwu on 2020.9.13
 */
public class LocalToHdfs {

    public static void main(String[] args) {
        String localFileName = args[0];
        String hdfsFileName = args[1];
        FileInputStream fis = null;
        FSDataOutputStream fsDataOutputStream = null;
        try{
            fis = new FileInputStream(localFileName);
            FileSystem fs = FileSystem.get(new URI(hdfsFileName),new Configuration());
            fsDataOutputStream = fs.create(new Path(hdfsFileName), new Progressable() {
                @Override
                public void progress() {
                    System.out.println(".");
                }
            });
            IOUtils.copyBytes(fis,fsDataOutputStream,1024,true);
        }catch(IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }finally {
//            IOUtils.closeStream(fsDataOutputStream);
//            IOUtils.closeStream(fis);

        }

    }
}

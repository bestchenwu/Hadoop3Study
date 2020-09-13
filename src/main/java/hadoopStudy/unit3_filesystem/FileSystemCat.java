package hadoopStudy.unit3_filesystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 使用{@link FileSystem}来获取文件内容
 *
 * @author chenwu on 2020.9.13
 */
public class FileSystemCat {

    public static void main(String[] args) {
        InputStream is = null;
        try{
            String uri = args[0];
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI(uri),conf);
            is = fs.open(new Path(uri));
            IOUtils.copyBytes(is,System.out,1024);
            ((FSDataInputStream) is).seek(0);
            System.out.println("=======");
            IOUtils.copyBytes(is,System.out,1024);
        }catch(URISyntaxException | IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeStream(is);
        }
    }
}

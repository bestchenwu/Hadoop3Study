package hadoopStudy.unit3_filesystem;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 接受一个hdfs的url，输出到{@link System#out}
 *
 * @author chenwu on 2020.9.13
 */
public class URLCat {

    public static void main(String[] args) {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        InputStream is = null;
        try{
            is = new URL(args[0]).openStream();
            IOUtils.copyBytes(is,System.out,1024);
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeStream(is);
        }
    }
}

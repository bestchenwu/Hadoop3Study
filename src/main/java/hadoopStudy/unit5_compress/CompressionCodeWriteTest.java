package hadoopStudy.unit5_compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 压缩code的写测试类
 *
 * @author chenwu on 2020.9.20
 */
public class CompressionCodeWriteTest {

    public static void main(String[] args) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        CompressionOutputStream outputStream = null;
        try{
            Configuration conf = new Configuration();
            CompressionCodec compressionCodec = ReflectionUtils.newInstance(GzipCodec.class, conf);
            fos = new FileOutputStream("/data/problem/hadoop3.2/compress.gz",false);
            //如果需要反复运用到解压缩
            Compressor compressor = CodecPool.getCompressor(compressionCodec);
            outputStream = compressionCodec.createOutputStream(fos,compressor);
            fis = new FileInputStream("/data/problem/hadoop3.2/local.txt");
            IOUtils.copyBytes(fis,outputStream,1024);
            outputStream.flush();
            outputStream.finish();
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeStream(outputStream);
            IOUtils.closeStream(fos);
            IOUtils.closeStream(fis);
        }

    }
}

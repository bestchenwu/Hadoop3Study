package hadoopCommonStudy.SerializableStudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 获取hadoop的压缩与反压缩
 *
 * @author chenwu on 2022.5.12
 */
public class HadoopCompressTest {

    @Test
    public void testCompressOutFile() throws Exception{
        Configuration conf = new Configuration();
        File file = new File("D:\\track_path_prepare.sql");
        String codecClassName = "org.apache.hadoop.io.compress.GzipCodec";
        Class<CompressionCodec> codeC = (Class<CompressionCodec>)Class.forName(codecClassName);
        CompressionCodec compressionCodec = ReflectionUtils.newInstance(codeC, conf);
        Compressor compressor = CodecPool.getCompressor(compressionCodec);
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos =  new FileOutputStream("D:\\test_"+codecClassName+".txt");
        CompressionOutputStream cos = compressionCodec.createOutputStream(fos,compressor);
        IOUtils.copyBytes(fis,cos,2048);
        fis.close();
        cos.close();
    }

    @Test
    public void testCompressInFile() throws Exception{
        FileInputStream fis = new FileInputStream("D:\\test_org.apache.hadoop.io.compress.GzipCodec.txt");
        FileOutputStream fos = new FileOutputStream("D:\\GzipCodec.txt",true);
        Configuration conf = new Configuration();
        String codecClassName = "org.apache.hadoop.io.compress.GzipCodec";
        Class<CompressionCodec> codeC = (Class<CompressionCodec>)Class.forName(codecClassName);
        CompressionCodec compressionCodec = ReflectionUtils.newInstance(codeC, conf);
        CompressionInputStream cis = compressionCodec.createInputStream(fis);
        IOUtils.copyBytes(cis,fos,1024);
        fos.close();
        cis.close();
    }
}

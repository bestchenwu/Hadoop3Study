package hadoopStudy.unit5_compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;
import java.net.URI;

/**
 * 测试code压缩的读类
 *
 * @author chenwu on 2020.9.20
 */
public class CompressionCodeReadTest {

    public static void main(String[] args) {
        String uri = "file:///data/problem/hadoop3.2/compress.gz";
        Configuration conf = new Configuration();
        InputStream is = null;
        OutputStream os = null;
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(uri), conf);
            Path inputPath = new Path(uri);
            //通过工厂模式以及文件名后缀的方式获得code
//            CompressionCodecFactory factory = new CompressionCodecFactory(conf);
//            CompressionCodec codec = factory.getCodec(inputPath);
            //通过反射类直接获得压缩codec的类
            CompressionCodec codec = ReflectionUtils.newInstance(GzipCodec.class, conf);
            //获取去掉压缩后缀的新文件名
            String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());
            //在读取压缩的文件的时候，会在当前文件的目录下新建一个.filename.crc的隐藏文件，用于保存每个文件块的校验和.
            is = codec.createInputStream(fileSystem.open(inputPath));
            os = fileSystem.create(new Path(outputUri), true);
            IOUtils.copyBytes(is, os, conf, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package hadoopStudy.unit3_filesystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FileStatusTest {

    @Test
    public void testFile() {
        try {
            String filePath = "hdfs://127.0.0.1:9000/hadoop/fromLocal";
            FileSystem fs = FileSystem.get(new URI(filePath), new Configuration());
            FileStatus[] fileStatuses = fs.listStatus(new Path(filePath));
            Assert.assertEquals(1, fileStatuses.length);
            FileStatus fileStatus = fileStatuses[0];
            Assert.assertEquals("sweet",fileStatus.getOwner());
            Assert.assertEquals(21l,fileStatus.getLen());
            //输出134217728,正好等于128MB(大写的B表示字节,一个字符表示一个字节,小写的b表示比特位,1B=8b)
            System.out.println("block size:"+fileStatus.getBlockSize());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}

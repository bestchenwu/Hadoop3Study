package HBaseStudy.unit2;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DiskStoreTest {

    @Test
    public void testReadFile() throws IOException {
        String filePath = "/data/logs/hbase/diskFile-";
        for(int i =0;i<=1;i++){
            String fileName = filePath+i;
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while(line!=null){
                //System.out.println(line);
                byte[] bytes = Bytes.toBytes(line);
                KeyValue keyValue = KeyValue.parseFrom(bytes);
                System.out.println(keyValue);
                line = br.readLine();
            }
            br.close();
        }
    }
}

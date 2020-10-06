package hadoopStudy.unit8_MapReduce_type;

import common.constants.SymbolConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 针对reduce输出阶段时候,key和value都不为空的情况
 *
 * @author chenwu on 2020.10.6
 */
public class TextOutputReader {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        Path path = new Path(args[0]);
        FileSystem fs = null;
        FSDataInputStream fsDataInputStream = null;
        Reader reader = null;
        BufferedReader br = null;
        try{
            fs = FileSystem.get(path.toUri(),conf);
            fsDataInputStream = fs.open(path);
            reader = new InputStreamReader(fsDataInputStream);
            br = new BufferedReader(reader);
            String line = br.readLine();
            while(line!=null){
                String[] splitArray = line.split(SymbolConstants.SYMBOL_TAB);
                System.out.printf("key=%s,value=%s\n",splitArray[0],splitArray[1]);
                line = br.readLine();
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeStream(br);
            try{
                fs.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

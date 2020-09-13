package hadoopStudy.unit3_filesystem;

import java.io.FileInputStream;
import java.io.IOException;

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
        try{
            fis = new FileInputStream(localFileName);
        }catch(IOException e){

        }

    }
}

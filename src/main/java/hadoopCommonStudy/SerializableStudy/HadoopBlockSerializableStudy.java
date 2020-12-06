package hadoopCommonStudy.SerializableStudy;


import org.apache.hadoop.hdfs.protocol.Block;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Hadoop的序列化机制
 *
 * @chenwu on 2020.11.1
 */
public class HadoopBlockSerializableStudy {

    public static void main(String[] args) throws IOException {
        //第一个参数是block的id 第二个参数是block的长度 第三个是时间戳
        Block block = new Block(11l,12l,13l);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        block.write(oos);
        System.out.println("array length:"+bos.toByteArray().length);
        oos.close();
    }
}

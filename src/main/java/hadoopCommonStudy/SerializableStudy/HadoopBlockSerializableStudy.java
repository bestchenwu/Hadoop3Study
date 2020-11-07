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
        Block block = new Block(11l,12l,13l);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        //如果使用java的序列化机制
        oos.writeObject(block);
        //使用Hadoop的序列化机制，数组长度只有24个字节
        //block.write(dos);
        dos.close();
        oos.close();
        System.out.println("array length:"+bos.toByteArray().length);
    }
}

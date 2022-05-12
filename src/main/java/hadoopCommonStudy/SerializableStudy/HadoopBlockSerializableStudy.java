package hadoopCommonStudy.SerializableStudy;


import org.apache.hadoop.hdfs.protocol.Block;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * Hadoop的序列化机制
 *
 * @chenwu on 2020.11.1
 */
public class HadoopBlockSerializableStudy {

    public static void main(String[] args) throws IOException {
        //第一个参数是block的id 第二个参数是block的长度 第三个是时间戳
        Block block = new Block(7806259420524417791l,39447755l,56736641l);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //以下为java的序列化方式
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        block.write(oos);
        System.out.println("array length:"+bos.toByteArray().length);
        oos.close();
        for(byte byte0 : bos.toByteArray()){
            System.out.print(byte0);
            System.out.print("\t");
        }

        //以下为Hadoop的序列化方式
//        DataOutputStream dos = new DataOutputStream(bos);
//        block.write(dos);
//        dos.close();
//        System.out.println("size="+bos.toByteArray().length);
//        for(byte byte0 : bos.toByteArray()){
//            System.out.print(byte0);
//            System.out.print("\t");
//        }
    }
}

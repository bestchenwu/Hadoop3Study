package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.Writable;

import java.io.*;

public class SerializeUtil {

    /**
     * 获取writable的内容,并序列化成byte数组
     *
     * @param writable
     * @return byte[]
     * @author chenwu on 2020.9.20
     */
    public static byte[] serializeWritable(Writable writable){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            writable.write(dos);
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    /**
     * 将datas写入到Writable
     *
     * @param writable
     * @param datas
     * @author chenwu on 2020.9.20
     */
    public static void serializeIntoWritable(Writable writable,byte[] datas){
        ByteArrayInputStream bis = new ByteArrayInputStream(datas);
        DataInputStream dis = new DataInputStream(bis);
        try {
            writable.readFields(dis);
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

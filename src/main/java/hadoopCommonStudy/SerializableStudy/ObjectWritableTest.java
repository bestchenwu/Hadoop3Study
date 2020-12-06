package hadoopCommonStudy.SerializableStudy;

import common.model.User;
import org.apache.hadoop.io.ObjectWritable;

import java.io.*;

/**
 * 测试{@link org.apache.hadoop.io.ObjectWritable}
 *
 * @author chenwu on 2020.12.6
 */
public class ObjectWritableTest {

    public static void main(String[] args) throws IOException {
        User user = new User();
        user.setId(11l);
        user.setName("test");
        ObjectWritable objectWritable = new ObjectWritable();
        objectWritable.set(user);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        objectWritable.write(dos);
        //测试能否从ObjectWritable里获取对象
        byte[] output = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(output);
        DataInputStream dis = new DataInputStream(bis);
        ObjectWritable anotherObjectWritable = new ObjectWritable();
        anotherObjectWritable.readFields(dis);
        User anotherObject = (User)anotherObjectWritable.get();
        System.out.println(anotherObject);
    }
}

package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class TextPairTest {

    @Test
    public void testTextPair() {
        TextPair textPair1 = new TextPair(new Text("hello"), new Text("second"));
        TextPair textPair2 = new TextPair(new Text("hello"), new Text("second"));
        Assert.assertEquals(true, textPair1.equals(textPair2));
        //测试输入和输出到流
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("/data/problem/hadoop3.2/textPair.txt", false);
            DataOutputStream dos = new DataOutputStream(fileOutputStream);
            textPair1.write(dos);
            dos.close();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        TextPair newPair = new TextPair();
        try {
            fis = new FileInputStream("/data/problem/hadoop3.2/textPair.txt");
            DataInputStream dis = new DataInputStream(fis);
            newPair.readFields(dis);
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Text first = newPair.getFirst();
        Assert.assertEquals("hello",first.toString());
        Text second = newPair.getSecond();
        Assert.assertEquals("second",second.toString());
    }
}

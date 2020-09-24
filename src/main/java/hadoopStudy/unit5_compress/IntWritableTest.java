package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.IntWritable;
import org.junit.Assert;
import org.junit.Test;

public class IntWritableTest {

    @Test
    public void testIntWritable(){
        IntWritable intWritable = new IntWritable(11);
        //因为一个int占4个字节
        byte[] bytes = SerializeUtil.serializeWritable(intWritable);
        Assert.assertEquals(4,bytes.length);
        //将字节重新序列化到IntWritable中
        IntWritable newIntWritable = new IntWritable();
        SerializeUtil.serializeIntoWritable(newIntWritable,bytes);
        Assert.assertEquals(11,newIntWritable.get());
    }
}

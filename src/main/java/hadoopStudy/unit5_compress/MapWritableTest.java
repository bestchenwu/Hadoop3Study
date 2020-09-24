package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link org.apache.hadoop.io.MapWritable}的测试类<br/>
 * 集合类型的测试
 *
 * @author chenwu on 2020.9.20
 */
public class MapWritableTest {

    @Test
    public void testMapWritable(){
        MapWritable mapWritable = new MapWritable();
        mapWritable.put(new IntWritable(4),new Text("haha"));
        mapWritable.put(new IntWritable(3),new Text("sweet"));
        Assert.assertEquals(new Text("sweet"),mapWritable.get(new IntWritable(3)));
        Assert.assertEquals(new Text("haha"),mapWritable.get(new IntWritable(4)));
    }
}

package hadoopStudy.unit5_compress;

import org.apache.hadoop.io.Text;
import org.junit.Assert;
import org.junit.Test;

public class TextTest {

    @Test
    public void testText(){
        Text text = new Text("hadoop");
        Assert.assertEquals(6,text.getLength());
        //text的charAt返回的是第几位对应的ASCII码
        Assert.assertEquals((int)'d',text.charAt(2));
    }
}

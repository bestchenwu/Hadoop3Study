package HBaseStudy.unit2;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class KeyValueTest {

    private String key = "lisisi";
    private String value = "love me";
    private Op op = Op.Put;
    private long sequeceId = SeqIdGenerator.getId();

    @Test
    public void testToBytes(){
        KeyValue keyValue = KeyValue.create(Bytes.toBytes(key),Bytes.toBytes(value),op,sequeceId);
        byte[] bytes = keyValue.toBytes();
        System.out.println(Arrays.toString(bytes));
    }

    @Test
    public void testParseBytes(){
        KeyValue keyValue = KeyValue.create(Bytes.toBytes(key),Bytes.toBytes(value),op,sequeceId);
        byte[] bytes = keyValue.toBytes();
        KeyValue newKeyValue = KeyValue.parseFrom(bytes);
        Assert.assertEquals(newKeyValue,keyValue);
        Assert.assertEquals(key,Bytes.toString(newKeyValue.getKey()));
        Assert.assertEquals(value,Bytes.toString(newKeyValue.getValue()));
        Assert.assertEquals(op,newKeyValue.getOp());
        Assert.assertEquals(sequeceId,newKeyValue.getSequenceId());
    }
}

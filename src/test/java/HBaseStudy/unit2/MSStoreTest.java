package HBaseStudy.unit2;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kerby.config.Conf;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MSStoreTest {

    private MSStore msStore;
    String key = "lisisi";
    String value = "loveMe";

    @Before
    public void init(){
        Config conf = Config.getDefault();
        conf.setMaxMemstoreSize(128);
        msStore = new MSStore(conf);
    }

    @Test
    public void testPut() throws IOException {
        for(int i = 0;i<10;i++){
            String keyTmp = key+i;
            String valueTmp = value+i;
            msStore.put(Bytes.toBytes(keyTmp),Bytes.toBytes(valueTmp));
        }
    }
}

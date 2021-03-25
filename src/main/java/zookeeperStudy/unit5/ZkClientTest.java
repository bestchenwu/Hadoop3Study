package zookeeperStudy.unit5;

import org.junit.Test;
import zookeeperStudy.client.ZkClient;

import java.io.IOException;

public class ZkClientTest {

    @Test
    public void testZkClientCreate(){
        try{
            ZkClient zkClient = new ZkClient("127.0.0.1:2181",1000*5);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}

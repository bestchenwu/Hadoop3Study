package zookeeperStudy.unit5;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;
import zookeeperStudy.client.ZkClient;
import zookeeperStudy.client.ZkClientConstants;

import java.io.IOException;

public class ZkClientTest {

    //@Test
    public void testZkClientCreate() {
        try {
            ZkClient zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testZkClientCreateWithPassword() {
        try {
            ZkClient zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5, zkClient.getSessionId(),
                    zkClient.getSessionPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testCreateDataSync() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            zkClient.createDataSync("/zk-book1", "haha".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDataASync() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            zkClient.createDateAsync("/zk-book2", "success".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT_SEQUENTIAL, ZkClientConstants.callback, "I am a context");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            Thread.sleep(10*1000);
        }catch(InterruptedException e){

        }

    }
}

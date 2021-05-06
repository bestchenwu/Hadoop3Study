package zookeeperStudy.unit5;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;
import zookeeperStudy.client.ZkClient;
import zookeeperStudy.client.ZkClientConstants;

import java.io.IOException;
import java.util.List;

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

    //@Test
    public void testCreateDataASync() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            zkClient.createDataAsync("/zk-book2", "success".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT_SEQUENTIAL, ZkClientConstants.callback, "I am a context");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {

        }
    }

    //@Test
    public void testDeleteData() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //当指定错误的节点版本时候,会报KeeperErrorCode = BadVersion for /zk-book20000000005这样的错误
            zkClient.deleteData("/zk-book20000000005", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {

        }
    }

    //@Test
    public void testGetChildrenWithOrNotWatch() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "/zk-book";
        try {
            //对节点path注册了一个子节点变更的事件通知
            List<String> childPathList = zkClient.getChildrenWithOrNotWatch(path, true);
            System.out.println("childPathList:" + childPathList);
            zkClient.createDataSync("/zk-book/testWatch", "testWatch".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testSetData() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "/zk-book1";
        Stat stat = new Stat();
        try {
            String data = zkClient.getData(path, stat);
            System.out.println("before data:" + data + ",version=" + stat.getVersion());
            zkClient.setData(path, "haha2".getBytes(), stat.getVersion());
            data = zkClient.getData(path, stat);
            System.out.println("after data:" + data + ",version=" + stat.getVersion());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testExists() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Stat stat = zkClient.existData("/zk-book1", null);
            System.out.println("version:" + stat.getVersion());
            stat = zkClient.existData("/zk-book111", null);
            Assert.assertNull(stat);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAuth() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zkClient.addAuth("digest", "foo:true".getBytes());
        String path = "/zk-book123";
        try {
            zkClient.createDataSync(path, "haha5".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        ZkClient zkClient1 = null;
        Stat stat = new Stat();
        try {
            zkClient1 = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果是在zookeeper命令行端,就需要执行类似于这样的addauth digest foo:true语句
        zkClient1.addAuth("digest", "foo:true".getBytes());
        try {
            String data = zkClient1.getData(path, stat);
            System.out.println("data1:" + data);
        } catch (KeeperException | InterruptedException e) {
            throw  new RuntimeException(e);
        }
        ZkClient zkClient2 = null;
        try {
            zkClient2 = new ZkClient("127.0.0.1:2181", 1000 * 5);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
        zkClient2.addAuth("digest", "foo:false".getBytes());
        try {
            String data = zkClient2.getData(path, stat);
            System.out.println("data2:" + data);
        } catch (KeeperException | InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }
}

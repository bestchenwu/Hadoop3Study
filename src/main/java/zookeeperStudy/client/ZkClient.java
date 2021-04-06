package zookeeperStudy.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper统一的client
 *
 * @author chenwu on 2021.3.25
 */
public class ZkClient implements Watcher {

    //允许一个或者多个线程等待的同步辅助
    //CountDownLatch的一个非常典型的应用场景是：有一个任务想要往下执行，但必须要等到其他的任务执行完毕后才可以继续往下执行。
    private static CountDownLatch connectedSemphore = new CountDownLatch(1);
    private ZooKeeper zookeeper;

    public ZkClient(String connectedString, int sessionTimeout) throws IOException {
        zookeeper = new ZooKeeper(connectedString, sessionTimeout, this);
        System.out.println("zookeeper state:"+zookeeper.getState());
        try{
            //如果当前计数器为0,则立即返回
            //如果当前计数器>0  则等待计数器为0(外部调用countDown)或者线程被打断
            connectedSemphore.await();
        }catch(Exception e){

        }
        System.out.println("zookeeper connected is established");
    }

    public ZkClient(String connectedString, int sessionTimeout,long sessionId,byte[] sessionPassword) throws IOException {
        zookeeper = new ZooKeeper(connectedString, sessionTimeout, this,sessionId,sessionPassword);
        try{
            Thread.sleep(100*1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public long getSessionId(){
        return zookeeper.getSessionId();
    }

    public byte[] getSessionPassword(){
        return zookeeper.getSessionPasswd();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("received event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            System.out.println("connected at "+ LocalTime.now());
            connectedSemphore.countDown();//将当前计数器-1  如果计数达到0,则释放所有等待的线程
        }else if(Event.KeeperState.Disconnected == event.getState()){
            System.out.println("disconnected at "+ LocalTime.now());
        }
    }

    /**
     * 利用同步的方法创建节点
     *
     * @param path
     * @param data
     * @param acl
     * @param createMode
     * @throws KeeperException
     * @throws InterruptedException
     * @author chenwu on 2021.3.26
     */
    public void createDataSync(String path, byte[] data, List<ACL> acl,
                           CreateMode createMode) throws KeeperException,InterruptedException {
        zookeeper.create(path,data,acl,createMode);
    }

    /**
     * 异步创建节点
     *
     * @param path
     * @param data
     * @param acl
     * @param createMode
     * @param callback
     * @param ctx
     * @author chenwu on 2021.4.1
     */
    public void createDataAsync(String path, byte[] data, List<ACL> acl,
                                CreateMode createMode, AsyncCallback.StringCallback callback, Object ctx){
            zookeeper.create(path,data,acl,createMode,callback,ctx);
    }

    /**
     * 删除节点内容
     *
     * @param path
     * @param version
     * @author chenwu on 2021.4.6
     */
    public void deleteData(String path,int version) throws KeeperException, InterruptedException {
        zookeeper.delete(path,version);
    }
}

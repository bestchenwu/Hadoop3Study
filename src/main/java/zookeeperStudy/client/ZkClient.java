package zookeeperStudy.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.time.LocalTime;
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
}

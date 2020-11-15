package zookeeperStudy.util;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Zookeeper的工具类
 *
 * @author chenwu on 2020.11.14
 */
public class ZookeeperUtil {

    private static ZooKeeper zkClient;

    public static ZooKeeper getInstance(String hostPorts) throws IOException {
        synchronized (ZookeeperUtil.class){
            if(zkClient == null){
                zkClient = new ZooKeeper(hostPorts,10,null);
            }
        }
        return zkClient;
    }
}

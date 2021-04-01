package zookeeperStudy.client;

import org.apache.zookeeper.AsyncCallback;

/**
 * zk的client链接常量类
 *
 * @author chenwu on 2021.4.1
 */
public class ZkClientConstants {

    public static AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("create path result:[" + rc + "," + path + "," + ctx + "," + name + "]");
        }
    };
}

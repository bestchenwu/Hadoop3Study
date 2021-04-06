package zookeeperStudy.client;

import org.apache.zookeeper.AsyncCallback;

/**
 * zk的client链接常量类
 *
 * @author chenwu on 2021.4.1
 */
public class ZkClientConstants {

    public static AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {

        /**
         * @param rc ResultCode 服务端响应码
         * @param path 节点路径
         * @param ctx 上下午参数
         * @param name 服务端实际创建的节点名
         */
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("create path result:[" + rc + "," + path + "," + ctx + "," + name + "]");
        }
    };
}

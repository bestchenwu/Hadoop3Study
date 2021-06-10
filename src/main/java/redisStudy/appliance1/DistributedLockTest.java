package redisStudy.appliance1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redisStudy.common.RedisClient;

/**
 * redis分布式锁的测试类
 *
 * @author chenwu on 2021.6.10
 */
public class DistributedLockTest {

    public static void main(String[] args) {
         String key = "sweet";
        RedisClient redisClient;
        String lockKey = "lock1";
        redisClient = RedisClient.getInstance("redisStudy/redis.conf");
        WriteThread thread1 = new WriteThread(redisClient,key,lockKey);
        thread1.setName("thread1");
        WriteThread thread2 = new WriteThread(redisClient,key,lockKey);
        thread2.setName("thread2");
        thread1.start();
        thread2.start();
    }

}

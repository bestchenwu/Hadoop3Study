package redisStudy.appliance4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redisStudy.common.RedisClient;

public class HyperLogLogTest {

    private RedisClient redisClient;
    private String key = "jack";

    @Before
    public void init() {
        redisClient = RedisClient.getInstance("redisStudy/redis.conf");
    }

    @Test
    public void testHyperLogLog(){
        for(int i = 0;i<1000;i++){
            redisClient.pfadd(key,new String[]{key+i});
        }
        System.out.println("count="+redisClient.pfcount(key));
    }

    @After
    public void close() {
        redisClient.close();
    }
}

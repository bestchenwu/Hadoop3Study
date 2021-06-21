package redisStudy.expand7;

import org.junit.Test;
import redisStudy.common.RedisClient;

public class ExecuteWithJedisTest {

    @Test
    public void testExecuteWithJedis(){
        RedisClient redisClient = RedisClient.getInstance("redisStudy/redis.conf");
        redisClient.executeSafety(redis->{System.out.println("sweet="+redis.get("sweet"));});
        redisClient.close();
    }
}

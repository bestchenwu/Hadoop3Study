package redisStudy.appliance6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redisStudy.common.RedisClient;

public class SimpleRateLimiterTest {

    private SimpleRateLimiter simpleRateLimiter;

    @Before
    public void init() {
        RedisClient redisClient = RedisClient.getInstance("redisStudy/redis.conf");
        simpleRateLimiter = new SimpleRateLimiter(redisClient);
    }

    @Test
    public void testIsActionAllowed() {
        String userId = "sweet";
        String actionKey = "reply";
        int period = 2;
        int maxCount = 5;
        for (int i = 0; i < 20; i++) {
            boolean isAllowed = simpleRateLimiter.isActionAllowed(userId, actionKey, period, maxCount);
            if (isAllowed) {
                System.out.println(i + " is allowded:" + isAllowed);
            } else {
                System.out.println(i + " is not allowded:" + isAllowed);
            }
            if (i % 4 == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @After
    public void close() {
        simpleRateLimiter.close();
    }
}

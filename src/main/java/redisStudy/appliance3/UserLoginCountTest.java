package redisStudy.appliance3;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redisStudy.common.RedisClient;

/**
 * 统计用户登录多少天
 *
 * @author chenwu on 2021.6.15
 */
public class UserLoginCountTest {

    private RedisClient redisClient;

    @Before
    public void init() {
        redisClient = RedisClient.getInstance("redisStudy/redis.conf");
    }

    @Test
    public void testLoginCount() {
        String user = "sweet";
        //用户在第2天 第10天，第101天登录
        redisClient.setbit(user
                , 2, true);
        redisClient.setbit(user
                , 5, true);
        redisClient.setbit(user
                , 10, true);
        redisClient.setbit(user
                , 15, true);
        redisClient.setbit(user
                , 101, true);
        //设置完成后
        // 00100100 00100001
        Long bitcount = redisClient.bitcount(user);
        //统计用户总登录天数
        Assert.assertEquals(5l,bitcount.longValue());
        //用户第0天到第8天的登录天数(也就是第一字节)
        bitcount = redisClient.bitcount(user,0,0);
        Assert.assertEquals(2l,bitcount.longValue());
        //统计第0到第16天的登录天数
        bitcount = redisClient.bitcount(user,0,1);
        Assert.assertEquals(4l,bitcount.longValue());
    }

    @After
    public void close() {
        redisClient.close();
    }
}

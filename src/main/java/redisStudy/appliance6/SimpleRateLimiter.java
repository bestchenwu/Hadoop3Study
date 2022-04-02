package redisStudy.appliance6;

import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redisStudy.common.RedisClient;

/**
 * 利用redis的zset来实现滑动窗口限流
 *
 * @author chenwu on 2021.6.16
 */
public class SimpleRateLimiter {

    private RedisClient redisClient;

    public SimpleRateLimiter(RedisClient redisClient){
        this.redisClient = redisClient;
    }

    /**
     * 验证用户在一分钟内指定的actionKey是否超过了最大次数
     *
     * @param userId
     * @param actionKey
     * @param period
     * @param maxCount
     * @return
     */
    public boolean isActionAllowed(String userId,String actionKey,int period,int maxCount){
        String key = String.format("%s_%s",userId,actionKey);
        long currentTime = System.currentTimeMillis();
        Pipeline pipelined = redisClient.getJedis().pipelined();
        pipelined.zadd(key,currentTime,String.valueOf(currentTime));
        pipelined.zremrangeByScore(key,0,currentTime-period*1000);
        pipelined.expire(key,period+1);
        Response<Long> count = pipelined.zcard(key);
        pipelined.sync();
        pipelined.close();
        return count.get()<=maxCount;
    }

    public void close(){
        this.redisClient.close();
    }
}

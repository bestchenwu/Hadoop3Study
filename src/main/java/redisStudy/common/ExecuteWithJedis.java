package redisStudy.common;

import redis.clients.jedis.Jedis;

public interface ExecuteWithJedis {

    public void execute(Jedis jedis);
}

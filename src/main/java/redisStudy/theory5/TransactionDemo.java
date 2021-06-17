package redisStudy.theory5;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redisStudy.common.RedisClient;

import java.util.List;

public class TransactionDemo {

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.getInstance("redisStudy/redis.conf");
        String key = "sweet_account";
        redisClient.setNx(key, "5");
        Integer result = increaseAccount(redisClient, key);
        System.out.println("new value:" + result);
        redisClient.close();
    }

    private static Integer increaseAccount(RedisClient redisClient, String key) {
        Jedis jedis = redisClient.getJedis();
        while (true) {
            String watchResult = jedis.watch(key);
            if (watchResult == null) {
                break;
            }
            String value = redisClient.getString(key);
            int newValue = 2 * Integer.parseInt(value);
            Transaction tx = jedis.multi();
            tx.set(key, String.valueOf(newValue));
            List<Object> exec = tx.exec();
            if (exec != null) {
                //执行成功了
                break;
            }

        }
        //jedis.close();
        return Integer.parseInt(jedis.get(key));
    }
}

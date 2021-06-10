package redisStudy.appliance1;

import redisStudy.common.RedisClient;

import java.util.Random;

public class WriteThread extends Thread {

    private RedisClient redisClient;
    private String key;
    private String lockKey;
    private Random random = new Random(47);

    public WriteThread(RedisClient redisClient, String key, String lockKey) {
        this.redisClient = redisClient;
        this.key = key;
        this.lockKey = lockKey;
    }

    @Override
    public void run() {
        while (true) {
            //首先获取锁lock
            Long lockKeyValue = redisClient.setNx(this.lockKey, "lockKey");
            if (lockKeyValue.longValue() != 1l) {
                try {
                    Thread.sleep((long) random.nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //然后执行指定操作
                long value = redisClient.getString(key)==null?0:Long.parseLong(redisClient.getString(key));
                System.out.println(getName() + " current value=" + value);
                redisClient.incr(key);
                long newValue = Integer.parseInt(redisClient.getString(key));
                System.out.println(getName() + " newValue =" + value);
                //然后释放锁
                redisClient.deleteKey(lockKey);
                if (newValue - value != 1) {
                    throw new IllegalStateException("newValue=" + newValue + ",value=" + value + " is not valid");
                }
            }
        }
    }
}

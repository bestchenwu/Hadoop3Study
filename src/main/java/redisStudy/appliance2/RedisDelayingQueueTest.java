package redisStudy.appliance2;

import redisStudy.common.RedisClient;

public class RedisDelayingQueueTest {

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.getInstance("redisStudy/redis.conf");
        String queueKey = "queue";
        final RedisDelayingQueue<String> queue = new RedisDelayingQueue<>(redisClient,queueKey);
        Thread producer = new Thread(){

            @Override
            public void run() {
                for(int i = 0;i<10;i++){
                    queue.delay("message:"+i);
                }
            }
        };
        Thread consumer = new Thread(){

            @Override
            public void run() {
                queue.loop();
            }
        };
        producer.start();
        consumer.start();
        try{
            producer.join();
            Thread.sleep(5*1000);
            consumer.interrupt();
            consumer.join();
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}

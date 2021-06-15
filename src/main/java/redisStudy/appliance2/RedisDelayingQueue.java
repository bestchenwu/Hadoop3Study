package redisStudy.appliance2;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import redisStudy.common.RedisClient;

import java.util.Set;
import java.util.UUID;

/**
 * redis延时队列
 *
 * @param <T>
 * @author chenwu on 2021.6.11
 */
public class RedisDelayingQueue<T> {

    private RedisClient redisClient;
    private String queueKey;

    public RedisDelayingQueue(RedisClient redisClient,String queueKey){
        this.redisClient = redisClient;
        this.queueKey = queueKey;
    }

    /**
     * 将消息对象塞入延迟队列
     *
     * @param msg
     */
    public void delay(T msg) {
        TaskItem<T> item = new TaskItem<>();
        item.id = UUID.randomUUID().toString();
        item.msg = msg;
        String jsonStr = JSON.toJSONString(item);
        redisClient.zadd(queueKey,jsonStr,System.currentTimeMillis()+500);
    }

    /**
     * 试图取出消息对象
     */
    public void loop() {
        while(!Thread.interrupted()){
            Set<String> zset = redisClient.zrangebyscore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if(CollectionUtils.isEmpty(zset)){
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
                continue;
            }
            String value = zset.iterator().next();
            Long zrem = redisClient.zrem(queueKey, new String[]{value});
            if(zrem!=null && zrem.longValue()==1l){
                //表明删除成功
                TaskItem taskItem = JSON.parseObject(value, TaskItem.class);
                decode(taskItem);
            }
        }
    }

    public void decode(TaskItem taskItem){
        System.out.println("I get message :"+taskItem);
    }
}

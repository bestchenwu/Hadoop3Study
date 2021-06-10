package redisStudy.common;

import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;

/**
 * Redis的客户端操作类
 *
 * @author chenwu on 2021.6.10
 */
public class RedisClient implements Closeable {

    private static RedisClient instance = null;
    private Jedis jedis;

    private RedisClient(String propertyFile){
        Properties property = new Properties();
        InputStream redisStream = ClassLoader.getSystemResourceAsStream(propertyFile);
        try{
            property.load(redisStream);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        jedis = new Jedis(property.getProperty("redis.host"),Integer.parseInt(property.getProperty("redis.port")),
                Integer.parseInt(property.getProperty("redis.timeout")));
    }

    public static RedisClient getInstance(String propertyFile){
        synchronized (RedisClient.class){
            if(instance==null){
                instance = new RedisClient(propertyFile);
            }
            return instance;
        }
    }

    public void setString(String key,String value){
        jedis.set(key,value);
    }

    public String getString(String key){
        return jedis.get(key);
    }

    public void hset(String key,String field,String value){
        jedis.hset(key,field,value);
    }

    public String hget(String key,String field){
        return jedis.hget(key,field);
    }

    public Map<String,String> hgetAll(String key){
        return jedis.hgetAll(key);
    }

    @Override
    public void close(){
        jedis.close();
    }

}

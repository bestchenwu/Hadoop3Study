package redisStudy.common;

import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
    private JedisPool pool;

    private void initializePool() {
        //redisURL 与 redisPort 的配置文件
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数（100个足够用了，没必要设置太大）
        config.setMaxTotal(20);
        //最大空闲连接数
        config.setMaxIdle(5);
        //获取Jedis连接的最大等待时间（50秒）
        config.setMaxWaitMillis(50 * 1000);
        //在获取Jedis连接时，自动检验连接是否可用
        config.setTestOnBorrow(true);
        //在将连接放回池中前，自动检验连接是否有效
        config.setTestOnReturn(true);
        //自动测试池中的空闲连接是否都是可用连接
        config.setTestWhileIdle(true);
        //创建连接池
        pool = new JedisPool(config, "localhost",6379);
    }

    public Jedis getJedis() {

        if (pool == null) {
            initializePool();
        }
        //如果没有以下代码会造成初始化的jedis拿不到 jedis对象
        Jedis jedis = null;
        try {
            if (pool != null) {
                jedis = pool.getResource();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    /**
     * 释放Jedis资源
     *
     * @param jedis
     */
    public void returnResource(Jedis jedis) {
        if (null != jedis) {
            //((GenericObjectPool)pool).returnResourceObject(jedis);
        }
    }


    private RedisClient(String propertyFile){
        Properties property = new Properties();
        InputStream redisStream = ClassLoader.getSystemResourceAsStream(propertyFile);
        try{
            property.load(redisStream);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        if(Integer.parseInt(property.getProperty("redis.pool.enable"))==1){
            //开启连接池
            initializePool();
        }else{
            jedis = new Jedis(property.getProperty("redis.host"),Integer.parseInt(property.getProperty("redis.port")),
                    Integer.parseInt(property.getProperty("redis.timeout")));
        }

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
        if(jedis==null){

        }else{
            jedis.set(key,value);
        }

    }

    public String getString(String key){
        return jedis.get(key);
    }

    public Long incr(String key){
        return jedis.incr(key);
    }

    public Long setNx(String key,String value){
        Long setnx = jedis.setnx(key, value);
        return setnx;
    }

    /**
     * 给Key设定一个过期时间,单位是秒
     *
     * @param key
     * @param timeout
     */
    public void expireKey(String key,int timeout){
        jedis.expire(key,timeout);
    }

    public void deleteKey(String key){
        jedis.del(key);
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

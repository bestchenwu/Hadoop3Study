package redisStudy.common;

import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Map;
import java.util.Set;

/**
 * Redis的客户端操作类
 *
 * @author chenwu on 2021.6.10
 */
public class RedisClient implements Closeable {

    private static RedisClient instance = null;
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
            throw new RuntimeException(e);
        }
        return jedis;
    }

    /**
     * 释放Jedis资源
     *
     * @param jedis
     */
    public void closeJedis(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }


    private RedisClient(String propertyFile){
        Properties property = new Properties();
        InputStream redisStream = ClassLoader.getSystemResourceAsStream(propertyFile);
        try{
            property.load(redisStream);
        }catch(IOException e){
            throw new RuntimeException(e);
        }//开启连接池
        initializePool();
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
        Jedis jedis = getJedis();
        jedis.set(key,value);
        jedis.close();
    }

    public String getString(String key){
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;

    }

    public Long incr(String key){
        Jedis jedis = getJedis();
        Long incr = jedis.incr(key);
        jedis.close();
        return incr;
    }

    public Long setNx(String key,String value){
        Jedis jedis = getJedis();
        Long setnx = jedis.setnx(key, value);
        jedis.close();
        return setnx;
    }

    /**
     * 给Key设定一个过期时间,单位是秒
     *
     * @param key
     * @param timeout
     */
    public void expireKey(String key,int timeout){
        Jedis jedis = getJedis();
        jedis.expire(key,timeout);
        jedis.close();
    }

    public void deleteKey(String key){
        Jedis jedis = getJedis();
        jedis.del(key);
        jedis.close();
    }

    public void hset(String key,String field,String value){
        Jedis jedis = getJedis();
        jedis.hset(key,field,value);
        jedis.close();
    }

    public String hget(String key,String field){
        Jedis jedis = getJedis();
        String value = jedis.hget(key, field);
        jedis.close();
        return value;
    }

    public Map<String,String> hgetAll(String key){
        Jedis jedis = getJedis();
        Map<String, String> map = jedis.hgetAll(key);
        jedis.close();
        return map;
    }

    public void zadd(String key,String value,double score){
        Jedis jedis = getJedis();
        jedis.zadd(key,score,value);
        jedis.close();
    }

    public Set<String> zrangebyscore(String key, double min, double max, int offset, int count){
        Jedis jedis = getJedis();
        Set<String> zset = new HashSet<>(jedis.zrangeByScore(key, min, max, offset, count));
        jedis.close();
        return zset;
    }

    public Long zrem(String key,String[] members){
        Jedis jedis = getJedis();
        Long zrem = jedis.zrem(key, members);
        jedis.close();
        return zrem;
    }

    public Long zremrangeByScore(String key,double min,double max){
        Jedis jedis = getJedis();
        Long removeResult = jedis.zremrangeByScore(key,min,max);
        jedis.close();
        return removeResult;
    }

    public Long zcard(String key){
        Jedis jedis = getJedis();
        Long result = jedis.zcard(key);
        jedis.close();
        return result;
    }

    /**
     * 对某一位进行设置1/0
     *
     * @param key
     * @param bit
     * @param flag
     * @return 设置之前的值
     */
    public Boolean setbit(String key,long bit,boolean flag){
        Jedis jedis = getJedis();
        Boolean result = jedis.setbit(key,bit,flag);
        jedis.close();
        return result;
    }

    public Long bitcount(String key){
        Jedis jedis = getJedis();
        Long bitcount = jedis.bitcount(key);
        jedis.close();
        return bitcount;
    }

    /**
     * 统计[start,end]之间的1的个数
     * -1 表示最后一个位，而 -2 表示倒数第二个位
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long bitcount(String key,long start,long end){
        Jedis jedis = getJedis();
        Long bitcount = jedis.bitcount(key,start,end);
        jedis.close();
        return bitcount;
    }

    public Long pfadd(String key,String[] elements){
        Jedis jedis = getJedis();
        Long result = jedis.pfadd(key,elements);
        jedis.close();
        return result;
    }

    public long pfcount(String key){
        Jedis jedis = getJedis();
        long pfcount = jedis.pfcount(key);
        jedis.close();
        return pfcount;
    }

    /**
     * 安全的执行jedis的命令
     *
     * @param callable
     * @author chenwu on 2021.6.21
     */
    public void executeSafety(ExecuteWithJedis callable){
        Jedis jedis = getJedis();
        try{
            callable.execute(jedis);
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            jedis.close();
        }
    }

    @Override
    public void close(){
        pool.close();
    }

}

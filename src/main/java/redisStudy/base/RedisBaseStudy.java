package redisStudy.base;

import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redisStudy.Person;
import redisStudy.common.RedisClient;
import java.util.Map;

public class RedisBaseStudy {

    private String key = "person";
    private RedisClient redisClient;
    private String hashKey = "person1";

    @Before
    public void init(){
        redisClient = RedisClient.getInstance("redisStudy/redis.conf");
    }

    //@Test
    public void testPersonReadWrite(){
        Person person = new Person();
        person.setId(11l);
        person.setName("haha");
        redisClient.setString(key, JSON.toJSONString(person));
        String personStr = redisClient.getString(key);
        Person newPerson = JSON.parseObject(personStr, Person.class);
        Assert.assertEquals(11l,newPerson.getId().longValue());
        Assert.assertEquals("haha",newPerson.getName());
    }

    @Test
    public void testPersonHashGetSet(){
        Person person = new Person();
        person.setId(11l);
        person.setName("haha");
        redisClient.hset(hashKey,"id",person.getId().toString());
        redisClient.hset(hashKey,"name",person.getName());
        Map<String,String> map = redisClient.hgetAll(hashKey);
        Assert.assertEquals(11l,Long.parseLong(map.get("id")));
        Assert.assertEquals("haha",map.get("name"));
    }

    @After
    public void close(){
        redisClient.close();
    }
}

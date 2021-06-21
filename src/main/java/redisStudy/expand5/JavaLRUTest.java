package redisStudy.expand5;

import org.junit.Test;

import java.util.LinkedHashMap;

public class JavaLRUTest {

    @Test
   public void testLRUCache(){
        MyLRUCache cache = new MyLRUCache(3);
        cache.put("test",1);
        cache.put("test1",2);
        cache.put("test2",3);
        cache.put("test3",4);
        cache.put("test4",5);
        cache.get("test4");
        cache.put("test5", 5);
        System.out.println("cache="+cache);
   }

}

package redisStudy.expand5;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyLRUCache extends LinkedHashMap<String,Integer> {

    private int size;

    public MyLRUCache(int size){
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
        if(this.size()>size){
            return true;
        }else{
            return false;
        }
    }

    public void add(String key,Integer value){
        super.put(key,value);
    }

    public void get(String key){
        super.get(key);
    }


}

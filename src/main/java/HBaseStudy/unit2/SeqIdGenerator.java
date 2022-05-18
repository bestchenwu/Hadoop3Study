package HBaseStudy.unit2;

import java.util.concurrent.atomic.AtomicLong;

public class SeqIdGenerator {

    private static final AtomicLong creator = new AtomicLong(0);

    public static long getId(){
        synchronized (SeqIdGenerator.class){
            long id = creator.getAndAdd(1);
            return id;
        }
    }
}

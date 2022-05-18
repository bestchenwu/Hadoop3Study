package HBaseStudy.unit2;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MSStore implements MiniBase{

    private ExecutorService pool;
    private MemStore memStore;
    private DiskStore diskStore;
    private Config conf;

    public MSStore(Config conf){
        this.conf = conf;
        this.pool = Executors.newFixedThreadPool(conf.getMaxThreadPoolSize());
        this.diskStore = new DiskStore("/data/logs/hbase/");
        this.memStore = new MemStore(conf,new DiskStore.DefaultFlusher(diskStore),pool);
    }

    @Override
    public void put(byte[] key, byte[] value) throws IOException {
        this.memStore.add(KeyValue.createPut(key,value,SeqIdGenerator.getId()));
    }

    @Override
    public byte[] get(byte[] key) throws IOException {
        return new byte[0];
    }

    @Override
    public void delete(byte[] key) throws IOException {

    }

    @Override
    public Iterator<KeyValue> scan(byte[] startKey, byte[] stopKey) throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}

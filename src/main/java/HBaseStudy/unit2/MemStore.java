package HBaseStudy.unit2;

import scala.tools.nsc.Global;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemStore implements Closeable {

    private AtomicLong dataSize = new AtomicLong(0);

    private volatile ConcurrentSkipListMap<KeyValue,KeyValue> kvMap ;

    //写入的时候先拿到读锁  在切换mmstore的时候先拿到写锁
    private ReentrantReadWriteLock updateLock = new ReentrantReadWriteLock();
    private volatile ConcurrentSkipListMap<KeyValue, KeyValue> snapshot;
    private AtomicBoolean isSnapshotFlushing = new AtomicBoolean(false);

    private ExecutorService pool;

    private Config conf;

    private MiniBase.Flusher flusher ;

    public MemStore(Config conf, MiniBase.Flusher flusher, ExecutorService pool){
        this.conf = conf;
        this.pool = pool;
        this.flusher = flusher;
        this.kvMap = new ConcurrentSkipListMap<>();
    }

    public void add(KeyValue kv) throws IOException {
        flushIfNeeded(true);
        updateLock.readLock().lock();
        try{
            KeyValue prev;
            prev = kvMap.put(kv,kv);
            if(prev!=null){
                dataSize.addAndGet(kv.getSerializerLength()-prev.getSerializerLength());
            }else{
                dataSize.addAndGet(kv.getSerializerLength());
            }
        }finally {
            updateLock.readLock().unlock();
        }
        flushIfNeeded(false);
    }

    private void flushIfNeeded(boolean shouldBlocking) throws IOException {
        if(dataSize.get()>conf.getMaxMemstoreSize()){
            boolean waitFlag = true;
            waitFlag = isSnapshotFlushing.get();
            if(waitFlag){
                while(waitFlag){
                    try{
                        System.err.println("please wait flush complete");
                        Thread.sleep(10);
                    }catch(InterruptedException e){
                        throw new RuntimeException(e);
                    }
                    waitFlag = isSnapshotFlushing.get();
                }
                //throw new IOException("please wait flush complete");
            }else if(isSnapshotFlushing.compareAndSet(false,true)){
                pool.submit(new FLusherTask());
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.pool.shutdown();
    }

    class FLusherTask implements Runnable{

        public FLusherTask() {
            super();
        }

        @Override
        public void run() {
            updateLock.writeLock().lock();
            try{
                snapshot = kvMap;
                kvMap = new ConcurrentSkipListMap<>();
                dataSize.set(0l);
            }finally {
                updateLock.writeLock().unlock();
            }

            boolean success = false;
            for(int i =0 ;i<conf.getFlushMaxRetries();i++){
                NavigableSet<KeyValue> keyValues = snapshot.keySet();
                try{
                    flusher.flush(keyValues);
                }catch(IOException e){
                    System.out.println("retry times:"+i);
                    continue;
                }
                success = true;
                break;
            }

            if(success){
                snapshot = null;
                isSnapshotFlushing.compareAndSet(true,false);
            }
        }
    }
}

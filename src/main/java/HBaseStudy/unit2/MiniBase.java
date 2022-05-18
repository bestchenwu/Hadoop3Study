package HBaseStudy.unit2;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * mini hbase
 *
 * @author chenwu on 2022.5.18
 */
public interface MiniBase extends Closeable {

    void put(byte[] key,byte[] value) throws IOException;

    byte[] get(byte[] key) throws IOException;

    void delete(byte[] key) throws IOException;

    Iterator<KeyValue> scan(byte[] startKey, byte[] stopKey) throws IOException;

    interface Flusher {
        void flush(Collection<KeyValue> it) throws IOException;
    }

    abstract class Compactor extends Thread {
        public abstract void compact() throws IOException;
    }
}

package HBaseStudy.unit2;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class DiskStore {

    String filePath;

    public DiskStore(String filePath){
        this.filePath = filePath;
    }

    private AtomicInteger diskFileId = new AtomicInteger(0);

    private String getNextedFileName(){
        return filePath+"diskFile-"+diskFileId.getAndAdd(1);
    }

    public static class DefaultFlusher implements MiniBase.Flusher{

        private DiskStore diskStore ;

        public DefaultFlusher(DiskStore diskStore){
            this.diskStore = diskStore;
        }

        @Override
        public void flush(Collection<KeyValue> it) throws IOException {
            String fileName = diskStore.getNextedFileName();
            DiskFileWriter diskFileWriter = new DiskFileWriter(fileName);
            for(KeyValue keyValue : it){
                diskFileWriter.write(keyValue);
            }
            diskFileWriter.flush();
            diskFileWriter.close();
        }
    }
}

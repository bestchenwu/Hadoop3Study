package HBaseStudy.unit2;

import org.apache.hadoop.hbase.util.Bytes;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class DiskFileWriter implements Closeable {

    private FileOutputStream fos;

    public DiskFileWriter(String fileName) throws IOException {
        this.fos= new FileOutputStream(fileName,true);
    }

    public void write(KeyValue keyValue) throws IOException{
        byte[] bytes = keyValue.toBytes();
        fos.write(bytes);
        fos.write(Bytes.toBytes("\n"));
    }

    public void flush() throws IOException{
        this.fos.flush();
    }

    @Override
    public void close() throws IOException {
        this.fos.close();
    }
}

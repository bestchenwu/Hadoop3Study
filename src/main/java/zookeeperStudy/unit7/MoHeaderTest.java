package zookeeperStudy.unit7;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MoHeaderTest {

    public static void main(String[] args) throws IOException {
        String tag = "header";
        //序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BinaryOutputArchive boa = BinaryOutputArchive.getArchive(bos);
        MocHeader mocHeader = new MocHeader(122l,"haha");
        mocHeader.serialize(boa,tag);
        //包装成ByteBuffer 用于网络传输
        ByteBuffer byteBuffer = ByteBuffer.wrap(bos.toByteArray());
        //反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array());
        BinaryInputArchive bia = BinaryInputArchive.getArchive(bis);
        MocHeader newHeader = new MocHeader();
        newHeader.deserialize(bia,tag);
        bis.close();
        bos.close();
        System.out.println("newHeader:"+newHeader);
    }
}

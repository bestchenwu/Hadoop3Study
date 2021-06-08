package zookeeperStudy.unit7;

import org.apache.jute.InputArchive;
import org.apache.jute.OutputArchive;
import org.apache.jute.Record;

import java.io.IOException;

/**
 * 测试zookeeper里面的jute组件
 *
 * @author chenwu on 2021.6.8
 */
public class MocHeader implements Record {

    private long sessionId;
    private String type;

    public MocHeader() {

    }

    public MocHeader(long sessionId, String type) {
        this.sessionId = sessionId;
        this.type = type;
    }

    @Override
    public void serialize(OutputArchive archive, String tag) throws IOException {
        archive.startRecord(this, tag);
        archive.writeLong(sessionId, "sessionId");
        archive.writeString(type, "type");
        archive.endRecord(this, tag);
    }

    @Override
    public void deserialize(InputArchive archive, String tag) throws IOException {
        archive.startRecord(tag);
        sessionId = archive.readLong("sessionId");
        type = archive.readString("type");
        archive.endRecord(tag);
    }

    @Override
    public String toString() {
        return "MocHeader{" +
                "sessionId=" + sessionId +
                ", type='" + type + '\'' +
                '}';
    }
}

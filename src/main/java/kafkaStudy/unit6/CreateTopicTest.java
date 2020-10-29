package kafkaStudy.unit6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.kafka.common.message.CreateTopicsRequestData;
import org.apache.kafka.common.protocol.ApiKeys;
import org.apache.kafka.common.requests.CreateTopicsRequest;

/**
 * 使用kafka的创建topic协议创建topic
 *
 * @author chenwu on 2020.10.28
 */
public class CreateTopicTest {

    public static void main(String[] args) throws IOException {
        SocketService socketService = new SocketService();
        List<CreateTopicsRequestData.CreatableTopic> creatableTopics = Arrays.asList(new CreateTopicsRequestData.CreatableTopic().setName("test-create-topic").setNumPartitions(3).setReplicationFactor((short) 1));
        CreateTopicsRequestData data =  new CreateTopicsRequestData().setTopics(new CreateTopicsRequestData.CreatableTopicCollection(creatableTopics.iterator()));
        CreateTopicsRequest createTopicsRequest = new CreateTopicsRequest.Builder(data).build();
        ByteBuffer response = socketService.send("localhost", 9092, createTopicsRequest, ApiKeys.CREATE_TOPICS);
        System.out.println("response:"+response.toString());
    }
}

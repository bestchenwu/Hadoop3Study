package kafkaStudy.unit4;

import kafkaStudy.model.User;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class UserSerializer implements Serializer<User> {

    private ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public byte[] serialize(String topic, User data) {
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsString(data).getBytes();
        } catch (IOException e) {
            System.err.println(ExceptionUtils.getFullStackTrace(e));
            //bytes = new byte[0];
        }
        return bytes;
    }
}

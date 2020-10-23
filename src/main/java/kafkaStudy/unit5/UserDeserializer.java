package kafkaStudy.unit5;

import kafkaStudy.model.User;
import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class UserDeserializer implements Deserializer<User> {
    private ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public User deserialize(String topic, byte[] data) {
        if(data == null || data.length ==0){
            return null;
        }
        User user = null;
        try {
            user = objectMapper.readValue(data, User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}

package kafkaStudy.unit4;

import kafkaStudy.model.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * {@link User}的序列化发送测试类
 */
public class UserProducerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"kafkaStudy.unit4.UserSerializer");
        KafkaProducer<String,User> producer = new KafkaProducer<String, User>(properties);
        User user = new User("ma","liang",28,"guangzhou");
        producer.send(new ProducerRecord<>("user",user));
        producer.close();
    }

}

package kafkaStudy.unit5;

import kafkaStudy.model.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * {@link User}的消费测试类
 */
public class UserConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"test-user");
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"kafkaStudy.unit5.UserDeserializer");
        KafkaConsumer<String,User> consumer = new KafkaConsumer<String, User>(properties);
        consumer.subscribe(Arrays.asList("user"));
        try{
            while(true){
                ConsumerRecords<String, User> consumerRecords = consumer.poll(Duration.ofSeconds(10l));
                for(ConsumerRecord<String,User> consumerRecord : consumerRecords){
                    System.out.printf("partition=%d,offset=%d,value=%s\n",consumerRecord.partition(),consumerRecord.offset(),consumerRecord.value());
                }
            }
        }finally {
            consumer.close();
        }
    }
}

package kafkaStudy.unit4;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


public class ProducerTest {

    public static void main(String[] args) {
        Properties property = new Properties();
        property.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        property.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        Producer<String,String> producer = new KafkaProducer<String, String>(property);
        for(int i = 0;i<10;i++){
            producer.send(new ProducerRecord<String,String>("test0","hello1,"+String.valueOf(i),"hello1,"+String.valueOf(i)));
        }
        producer.close();
    }
}

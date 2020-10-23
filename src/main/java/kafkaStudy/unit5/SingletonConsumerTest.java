package kafkaStudy.unit5;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 指定若干parition给独立消费者的测试类
 *
 * @author chenwu on 2020.10.23
 */
public class SingletonConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"test-group");
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        //只订阅第一个分区
        List<PartitionInfo> partitionList = consumer.partitionsFor("test1").stream().filter(partitionInfo -> partitionInfo.partition() == 0).collect(Collectors.toList());
        List<TopicPartition> firstPartitionInfos = partitionList.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(),partitionInfo.partition())).collect(Collectors.toList());
        consumer.assign(firstPartitionInfos);
        try{
            while (true){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(10L));
                for(ConsumerRecord<String,String> consumerRecord : consumerRecords){
                    System.out.printf("partition=%d,offset=%d,value=%s\n",consumerRecord.partition(),consumerRecord.offset(),consumerRecord.value());
                }
            }
        }finally {
            consumer.close();
        }
    }
}

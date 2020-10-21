package kafkaStudy.unit5;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class ManualDetailOffsetConsumerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"consumer-test");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        //指定partition分配策略
        props.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,"org.apache.kafka.clients.consumer.StickyAssignor");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props,new StringDeserializer(),new StringDeserializer());
        consumer.subscribe(Arrays.asList("test1"));
        try{
            List<ConsumerRecord<String,String>> recordList =  new ArrayList<>();
            while(true){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5l));
                for(TopicPartition topicPartition : consumerRecords.partitions()){
                    recordList = consumerRecords.records(topicPartition);
                    //处理单个分区里的业务消息逻辑
                    decodeRecords(recordList);
                    //获取最后一条消息的位移
                    long offset = recordList.get(recordList.size()-1).offset();
                    //下一条消费的位移要在最后一条消息的位移的基础上+1
                    consumer.commitSync(Collections.singletonMap(topicPartition,new OffsetAndMetadata(offset+1)));
                }
            }
        }finally {
            consumer.close();
        }
    }

    private static void decodeRecords(List<ConsumerRecord<String,String>> recordList){
        //处理消息的业务逻辑
        for(ConsumerRecord<String,String> record : recordList){
            System.out.printf("key=%s,value=%s,partition=%d,offset=%d\n",record.key(),record.value(),record.partition(),record.offset());
        }
    }
}

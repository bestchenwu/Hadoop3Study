package kafkaStudy.unit5;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 消费者的测试类(手动提交位移)
 *
 * @author chenwu on 2020.10.21
 */
public class ManualOffsetConsumerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"consumer-test");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props,new StringDeserializer(),new StringDeserializer());
        consumer.subscribe(Arrays.asList("test1"));
        int minBatchSize = 50;
        try{
            List<ConsumerRecord<String,String>> recordList =  new ArrayList<>();
            while(true){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5l));
                for(ConsumerRecord<String,String> record : consumerRecords){
                    recordList.add(record);
                }
                if(recordList.size()>=minBatchSize){
                    decodeRecords(recordList);
                    consumer.commitAsync();
                    recordList.clear();
                }
            }
        }finally {
            consumer.close();
        }
    }

    private static void decodeRecords(List<ConsumerRecord<String,String>> recordList){
        //处理消息的业务逻辑
        for(ConsumerRecord<String,String> record : recordList){
            System.out.printf("key=%s,value=%s,offset=%d\n",record.key(),record.value(),record.offset());
        }
    }


}

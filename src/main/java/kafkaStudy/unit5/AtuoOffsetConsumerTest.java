package kafkaStudy.unit5;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 消费者测试(自动提交位移)
 *
 * @author chenwu on 2020.10.16
 */
public class AtuoOffsetConsumerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"consumer-test");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        //从最早的消息开始读
        //共有3种值 分别为latest(最新位移)、earliest(最早位移)、none
        //earliest
        //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        //latest
        //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        String topic = "test1";
        KafkaConsumer consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList(topic));
        try{
            while(true){
                ConsumerRecords<String,String> pollRecords = consumer.poll(Duration.ofSeconds(5l));
                for(ConsumerRecord record : pollRecords){
                    System.out.printf("topic=%s,key=%s,value=%s,offset=%d\n",record.topic(),record.key(),record.value(),record.offset());
                }
            }
        }finally {
            consumer.close();
        }

    }
}

package kafkaStudy.unit5;

import common.util.CommonJDBCUtil;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * consumer手动感知reblance的测试类
 *
 * @author chenwu on 2020.10.22
 */
public class ManualBalanceConsumerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"consumer-test");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        //指定partition分配策略
        props.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,"org.apache.kafka.clients.consumer.StickyAssignor");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props,new StringDeserializer(),new StringDeserializer());
        consumer.subscribe(Arrays.asList("test1"), new ConsumerRebalanceListener() {

            final AtomicLong totalReblanceMs = new AtomicLong(0l);
            final CommonJDBCUtil commonJdbcUtil = CommonJDBCUtil.getInstance();

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                for(TopicPartition topicPartition : partitions){
                    long offset = consumer.position(topicPartition);
                    //将该分区的offset保存
                    try{
                        saveOffset(topicPartition.topic(),topicPartition.partition(),offset);
                    }catch(SQLException e){
                        throw new RuntimeException(e);
                    }
                }
                totalReblanceMs.set(System.currentTimeMillis());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                for(TopicPartition topicPartition : partitions){
                    try{
                        long offset = getOffset(topicPartition.topic(), topicPartition.partition());
                        consumer.seek(topicPartition,offset);
                    }catch(SQLException e){
                        throw new RuntimeException(e);
                    }
                }
                long currentTime = System.currentTimeMillis();
                System.out.println("rebalance during time: "+(currentTime-totalReblanceMs.get()));
                totalReblanceMs.set(currentTime);
            }

            private void saveOffset(String topic,Integer partition,long offset) throws SQLException{
                commonJdbcUtil.insertTopicOffset(topic,partition,offset);
            }

            private long getOffset(String topic,Integer partition) throws SQLException{
                return commonJdbcUtil.getOffset(topic,partition);
            }
        });
        try{
            List<ConsumerRecord<String,String>> recordList =  new ArrayList<>();
            while(true){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5l));
                for(TopicPartition topicPartition : consumerRecords.partitions()){
                    recordList = consumerRecords.records(topicPartition);
                    //处理单个分区里的业务消息逻辑
                    decodeRecords(recordList);
                    long offset = recordList.get(recordList.size()-1).offset();
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

package kafkaStudy.unit7;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 使用{@link org.apache.kafka.clients.admin.AdminClient}来创建topic
 *
 * @author chenwu on 2020.10.29
 */
public class KafkaAdminUtil {

    private static AdminClient adminClient;

    static{
        Properties properties = new Properties();
        properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        adminClient = AdminClient.create(properties);
    }

    public static void createTopic(String topicName,int partitionNumber,short factorNumber){
        NewTopic newTopic = new NewTopic(topicName,partitionNumber,factorNumber);
        adminClient.createTopics(Arrays.asList(newTopic));
    }

    public static void deleteTopic(String topicName){
        adminClient.deleteTopics(Arrays.asList(topicName));
    }

    public static void scanTopicProperty(String topicName){
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(topicName));
        if(describeTopicsResult!=null){
            KafkaFuture<Map<String, TopicDescription>> all = describeTopicsResult.all();
            try {
                Map<String, TopicDescription> map = all.get();
                TopicDescription topicDescription = map.get(topicName);
                for(org.apache.kafka.common.TopicPartitionInfo topicPartitionInfo: topicDescription.partitions()){
                    System.out.printf("partition=%d",topicPartitionInfo.partition());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(){
        adminClient.close();
    }
}

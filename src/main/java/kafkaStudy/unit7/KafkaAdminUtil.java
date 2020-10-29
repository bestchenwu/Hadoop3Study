package kafkaStudy.unit7;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import java.util.Arrays;
import java.util.Properties;

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
        adminClient = KafkaAdminClient.create(properties);
    }

    public static void createTopic(String topicName,int partitionNumber,short factorNumber){
        NewTopic newTopic = new NewTopic(topicName,partitionNumber,factorNumber);
        adminClient.createTopics(Arrays.asList(newTopic));
    }
}

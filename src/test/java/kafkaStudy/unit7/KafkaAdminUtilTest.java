package kafkaStudy.unit7;

import org.junit.After;
import org.junit.Test;

public class KafkaAdminUtilTest {

//    @Test
//    public void testCreateTopic(){
//        KafkaAdminUtil.createTopic("test2",5,(short)1);
//    }

//    @Test
//    public void testDeleteTopic(){
//        KafkaAdminUtil.deleteTopic("test2");
//    }

    @Test
    public void testDescribeTopics(){
        KafkaAdminUtil.scanTopicProperty("test1");
    }

    @After
    public void close(){
        KafkaAdminUtil.close();
    }
}

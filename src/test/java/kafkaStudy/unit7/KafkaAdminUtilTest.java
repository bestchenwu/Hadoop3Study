package kafkaStudy.unit7;

import org.junit.Test;

public class KafkaAdminUtilTest {

    @Test
    public void testCreateTopic(){
        KafkaAdminUtil.createTopic("test2",5,(short)1);
    }
}

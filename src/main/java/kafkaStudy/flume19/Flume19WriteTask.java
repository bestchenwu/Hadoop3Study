package kafkaStudy.flume19;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 测试写入到kafka2.2的topic 并最终汇总到flume1.9
 */
public class Flume19WriteTask {

    public static void main(String[] args) {
        final Log log = LogFactory.getLog(Flume19WriteTask.class.getName());
        Properties property = new Properties();
        property.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        property.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String,String> producer = new KafkaProducer<String, String>(property);
        String topic = "test-flink";
        while(true){
            long timeStamp = System.currentTimeMillis();
            String value = "kafka2.2_"+timeStamp;
            ProducerRecord record = new ProducerRecord<String, String>(topic, value);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(exception==null){
                        if(log.isInfoEnabled()){
                            log.info(value);
                        }
                    }else{
                        log.error("error:"+value+":"+metadata.timestamp());
                    }
                }
            });
            try{
                Thread.sleep(1000*30);
            }catch(InterruptedException e ){
                e.printStackTrace();
                break;
            }
        }
    }
}

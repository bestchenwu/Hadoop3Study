package kafkaStudy.unit4;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 统计发送的成功消息和失败消息的次数
 *
 * @author chenwu on 2020.10.9
 */
public class CounterInterceptor implements ProducerInterceptor<String,String> {

    private int successCount = 0;
    private int failureCount = 0;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if(exception==null){
            successCount++;
        }else{
            failureCount++;
        }
    }

    @Override
    public void close() {
        System.out.println("send success count:"+successCount);
        System.out.println("send failure count:"+failureCount);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

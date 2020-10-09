package kafkaStudy.unit4;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class TimeStampPrependInterceptor implements ProducerInterceptor<String,String> {

    /**
     * 消息在序列化以计算分区之前调用该方法，可以修改record的内容
     *
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return new ProducerRecord<String,String>(record.topic(),record.partition(),record.key(),System.currentTimeMillis()+","+record.value());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

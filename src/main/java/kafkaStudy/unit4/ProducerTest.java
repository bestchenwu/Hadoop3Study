package kafkaStudy.unit4;

import kafkaStudy.model.User;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.RetriableException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 生产者示例
 *
 * @author chenwu on 2020.10.9
 */
public class ProducerTest {

    public static void main(String[] args) {
        Properties property = new Properties();
        property.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        property.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //设置序列化类
        //property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "kafkaStudy.unit4.UserSerializer");
        //设置压缩算法
        property.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
        //设置自定义分区
        property.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "kafkaStudy.unit4.AuditPartitioner");
        //设置消息拦截器
        List<String> list = Arrays.asList("kafkaStudy.unit4.TimeStampPrependInterceptor", "kafkaStudy.unit4.CounterInterceptor");
        property.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list);
        //测试消息序列化
        Producer<String,String> producer = new KafkaProducer<String, String>(property);
        for(int i = 1;i<=10;i++){
            producer.send(new ProducerRecord<String,String>("test1","haha"+i));
        }
        //测试消息序列化
//        Producer<String, User> producer = new KafkaProducer<String, User>(property);
//        User user = new User("rao","shanshan",18,"guanggu");
//        producer.send(new ProducerRecord<String,User>("user",user));
//        producer.send(new ProducerRecord<String,String>("test1","haha","haha"));
//        producer.send(new ProducerRecord<String,String>("test1","audit1","audit1"));
//        producer.send(new ProducerRecord<String,String>("test1","sweet","sweet"));
//        producer.send(new ProducerRecord<String,String>("test1","2audit","2audit"));
//        producer.send(new ProducerRecord<String,String>("test1","raoshanshan","raoshanshan"));
//        for (int i = 0; i < 10; i++) {
//            final String value = "hello1," + String.valueOf(i);
//            producer.send(new ProducerRecord<String, String>("test1", value), new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata metadata, Exception exception) {
//                    if (exception == null) {
//                        System.out.printf("send %s success",value);
//                    }else{
//                        if(exception instanceof RetriableException){
//                            System.err.println("an RetriableException happens,"+exception.getMessage());
//                        }else{
//                            System.err.println("an unRetriableException happens,"+exception.getMessage());
//                        }
//                    }
//                }
//            });
//        }
        producer.close();
    }
}

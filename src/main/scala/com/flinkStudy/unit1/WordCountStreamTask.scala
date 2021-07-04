package com.flinkStudy.unit1

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.api.scala._
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 使用kafka来接受消息
 *
 * @author chenwu on 2021.7.4
 */
object WordCountStreamTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val props = new Properties()
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.2.107:9092")
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val flinkStream = env.addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), props))
    val wordCountStream = flinkStream.map((_, 1)).keyBy(_._1).sum(1)
    //wordCountStream.print()
    wordCountStream.writeAsText("/data/logs/flink/flink_stream.txt",WriteMode.OVERWRITE)
    env.execute("WordCountStreamTask")
  }
}

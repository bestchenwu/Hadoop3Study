package com.flinkTheory.unit3

import com.flinkTheory.KafkaMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory

import java.util.Properties

object CoProcessFunctionTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic1 = "test"
    val topic2="test-flink"
    val mapFunction= new KafkaMapFunction()
    val kafkaInputStream1 = env.addSource(new FlinkKafkaConsumer[String](topic1, new SimpleStringSchema(), properties)).map(mapFunction)
    val kafkaInputStream2 = env.addSource(new FlinkKafkaConsumer[String](topic2, new SimpleStringSchema(), properties)).map(mapFunction)
    val res = kafkaInputStream1.connect(kafkaInputStream2).process(new CoProcessFunction[(String, Int), (String, Int), (String, Int)] {

      val log = LoggerFactory.getLogger("CoProcessFunctionTest")

      override def processElement1(value: (String, Int), ctx: CoProcessFunction[(String, Int), (String, Int), (String, Int)]#Context, out: Collector[(String, Int)]): Unit = {
        log.info("输出第一个流:" + value)
        out.collect(value)
      }

      override def processElement2(value: (String, Int), ctx: CoProcessFunction[(String, Int), (String, Int), (String, Int)]#Context, out: Collector[(String, Int)]): Unit = {
        log.info("输出第二个流:" + value)
        out.collect(value)
      }
    })
    res.print()
    env.execute("CoProcessFunctionTest")
  }
}

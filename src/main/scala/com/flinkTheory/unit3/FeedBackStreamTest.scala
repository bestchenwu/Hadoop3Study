package com.flinkTheory.unit3

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * flink 反馈流的测试<br/>
 * 可以参考:https://www.cnblogs.com/Springmoon-venn/p/13857002.html
 *
 * @author chenwu on 2022.2.19
 */
object FeedBackStreamTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic = "test"
    val kafkaInputStream = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val mapStream = kafkaInputStream.map(str => {
      val array = str.split(",")
      (array(0), array(1).toLong)
    })
    val iterableStream = mapStream.iterate(ds => {
      val dsMap = ds.map(item => {
        (item._1, item._2 + 1)
      }).keyBy(_._1).window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
        .process(new ProcessWindowFunction[(String, Long), (String, Long), String, TimeWindow] {
          override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[(String, Long)]): Unit = {
            val iter = elements.map(_._2)
            val sum = iter.reduce(_ + _)
            out.collect((key,sum))
          }
        })
      (dsMap.filter(_._2 < 100), dsMap.filter(_._2 > 100))
    })
    iterableStream.print("result:")
    env.execute("FeedBackStreamTest")
  }
}

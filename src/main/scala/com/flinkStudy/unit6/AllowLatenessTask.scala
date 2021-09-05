package com.flinkStudy.unit6

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

/**
 * 测试对于延迟数据的处理
 */
object AllowLatenessTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //第一条流来自于kafka
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink-coProcess")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic = "test-flink"
    val kafkaStream = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val stream = kafkaStream.map(item => {
      val array = item.split(",")
      (array(0), array(1).toLong)
    }).assignAscendingTimestamps(_._2).keyBy(_._1)
    val timeService = stream.timeWindow(Time.seconds(10l)).allowedLateness(Time.seconds(5))
    timeService.process(new UpdatingWindowFunction)
    env.execute("AllowLatenessTask")
  }

  class UpdatingWindowFunction extends ProcessWindowFunction[(String,Long),(String,Long,Int,String),String,TimeWindow]{
    override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[(String, Long, Int, String)]): Unit = {
      
    }
  }
}

package com.flinkTheory.unit4

import org.apache.commons.lang.StringUtils
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 5分钟一个窗口,平均每3个元素输出一次
 *
 * @author chenwu on 2022.3.20
 */
object Every3ElementOutputTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val statebackend = new FsStateBackend("hdfs://master:9000/flink/Every3ElementOutputTask", false);
    env.setStateBackend(statebackend)
    env.enableCheckpointing(30000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic1 = "test"
    val userBehaviorStream = env.addSource(new FlinkKafkaConsumer[String](topic1, new SimpleStringSchema(), properties)).filter(StringUtils.isNotBlank(_)).filter(_.split(",").size == 2).map(item => {
      val array = item.split(",")
      (array(0).toLong, array(1))
    })
    val res = userBehaviorStream.keyBy(_._1).window(TumblingProcessingTimeWindows.of(Time.minutes(5l))).trigger(new Every3ElementsOutputTrigger)
      .reduce((item1, item2) => {
        if (item1._2.compareTo(item2._2) > 0) {
          item1
        } else {
          item2
        }
      })
    res.print()
    env.execute("Every3ElementOutputTask")
  }
}

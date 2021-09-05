package com.flinkStudy.unit6

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 测试{@link CoProcessFunction} 低阶混合流
 *
 * @author chenwu on 2021.8.13
 */
object CoProcessFunctionTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    //第一条流来自于kafka
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink-coProcess")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic = "test-flink"
    val kafkaStream = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val stream1 = kafkaStream.map(item => {
      val array = item.split(",")
      (array(0), array(1))
    }).keyBy(_._1)
    val stream2 = env.fromElements(("sensor_1", 10 * 1000l), ("sensor_2", 60 * 1000l)).keyBy(_._1)
    val coStream = stream1.connect(stream2).process(new ReadFilter)
      val streamFileSinkFunction = StreamingFileSink.forRowFormat(new Path("/data/logs/flink/flink_coProcess/"), new SimpleStringEncoder[(String, String)]("UTF-8")).build()
    coStream.addSink(streamFileSinkFunction)
    env.execute("CoProcessFunctionTest")
  }

  class ReadFilter extends CoProcessFunction[(String, String), (String, Long), (String, String)] {

    lazy val forwardState: ValueState[Boolean] = getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("filter-switch", Types.of[Boolean]))


    override def processElement1(value: (String, String), ctx: CoProcessFunction[(String, String), (String, Long), (String, String)]#Context, out: Collector[(String, String)]): Unit = {
      //如果流2进入,并且在流2开始的时间窗内，就输出流1数据
      val currentTime = ctx.timerService().currentProcessingTime()
      println(s"s element1 is ${currentTime}")
      if (forwardState.value()) {
        out.collect(value)
      }
    }

    override def processElement2(value: (String, Long), ctx: CoProcessFunction[(String, String), (String, Long), (String, String)]#Context, out: Collector[(String, String)]): Unit = {
      forwardState.update(true)
      val currentTime = ctx.timerService().currentProcessingTime()
      println(s"currentTime is :${currentTime},and element1 will end at ${value._2} later")
      val time = currentTime + value._2
      ctx.timerService().registerEventTimeTimer(time)
    }

    override def onTimer(timestamp: Long, ctx: CoProcessFunction[(String, String), (String, Long), (String, String)]#OnTimerContext, out: Collector[(String, String)]): Unit = {
      forwardState.update(false)
    }
  }

}

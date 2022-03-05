package com.flinkTheory.unit3

import com.flinkTheory.KafkaMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.{CoProcessFunction, KeyedCoProcessFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 双流join(带状态的)<br/>
 * 如果流1先到,则输出流2
 * 同理是流2<br/>
 * 参考博客:https://www.cnblogs.com/mn-lily/p/14735934.html以及<br/>
 * https://blog.csdn.net/qq_36213530/article/details/120115707
 *
 * @author chenwu on 2022.3.5
 */
object CoProcessWithStateFunctionTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val statebackend = new FsStateBackend("hdfs://master:9000/flink/CoProcessWithStateFunctionTest", false);
    env.setStateBackend(statebackend)
    env.enableCheckpointing(3000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic1 = "test"
    val topic2 = "test-flink"
    val kafkaInputStream1 = env.addSource(new FlinkKafkaConsumer[String](topic1, new SimpleStringSchema(), properties)).map(item => (item, 1)).keyBy(_._1)
    val kafkaInputStream2 = env.addSource(new FlinkKafkaConsumer[String](topic2, new SimpleStringSchema(), properties)).map(item => (item, 2)).keyBy(_._1)
    val outTag1 = new OutputTag[String]("value1")
    val outTag2 = new OutputTag[String]("value2")
    val res = kafkaInputStream1.connect(kafkaInputStream2).process(new CoProcessFunction[(String, Int), (String, Int), String] {

      var valueState1: ValueState[String] = null;
      var valueState2: ValueState[String] = null;
      var timeState: ValueState[Long] = null

      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        valueState1 = getRuntimeContext.getState(new ValueStateDescriptor("valueState1", createTypeInformation[String]))
        valueState2 = getRuntimeContext.getState(new ValueStateDescriptor("valueState2", createTypeInformation[String]))
        timeState = getRuntimeContext.getState(new ValueStateDescriptor("valueState2", createTypeInformation[Long]))
      }

      override def processElement1(value: (String, Int), ctx: CoProcessFunction[(String, Int), (String, Int), String]#Context, out: Collector[String]): Unit = {
        val value2 = valueState2.value()
        if (value2 != null) {
          //说明value2已经到了
          val time = timeState.value()
          valueState2.clear()
          //删除触发器
          if (time != null) {
            ctx.timerService().deleteEventTimeTimer(time)
            timeState.clear()
          }
          out.collect("value1:" + value._1 + ",value2:" + value2)
        } else {
          valueState1.update(value._1)
          val lastTime = ctx.timestamp() + 2000l
          timeState.update(lastTime)
          ctx.timerService().registerProcessingTimeTimer(lastTime)
        }
      }

      override def processElement2(value: (String, Int), ctx: CoProcessFunction[(String, Int), (String, Int), String]#Context, out: Collector[String]): Unit = {
        val value1 = valueState1.value()
        if (value1 != null) {
          //说明value1已经到了
          val time = timeState.value()
          valueState1.clear()
          //删除触发器
          if (time != null) {
            ctx.timerService().deleteEventTimeTimer(time)
            timeState.clear()
          }
          out.collect("value2:" + value._1 + ",value1:" + value1)
        } else {
          valueState2.update(value._1)
          val lastTime = ctx.timestamp() + 2000l
          timeState.update(lastTime)
          ctx.timerService().registerProcessingTimeTimer(lastTime)
        }

      }

      override def onTimer(timestamp: Long, ctx: CoProcessFunction[(String, Int), (String, Int), String]#OnTimerContext, out: Collector[String]): Unit = {
        super.onTimer(timestamp, ctx, out)
        val value1 = valueState1.value()
        val value2 = valueState2.value()
        if (value1 != null) {
          ctx.output(outTag1, "outputTag1:" + value1.toString)
          valueState1.clear()
        }
        if (value2 != null) {
          ctx.output(outTag2, "outputTag2:" + value2.toString)
          valueState2.clear()
        }
        timeState.clear()
      }

      override def close(): Unit = super.close()
    })

    res.getSideOutput(outTag1).print()
    res.getSideOutput(outTag2).print()
    res.print()
    env.execute("CoProcessWithStateFunctionTest")
  }
}

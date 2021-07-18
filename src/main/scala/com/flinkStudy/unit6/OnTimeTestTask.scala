package com.flinkStudy.unit6

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

/**
 * 本章描述flink的keyProcessFunction的ontime函数
 *
 * @author chenwu on 2021.7.18
 */
object OnTimeTestTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.107:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val topic = ""
    val kafkaConsumer = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val timeModelStream = kafkaConsumer.map(item => {
      val array = item.split(",")
      TimeModel(array(0).toLong, array(1).toLong,array(2).toDouble)
    })
    val keyByStream = timeModelStream.keyBy(_.id).process(new KeyedProcessFunction[Long, TimeModel, String] {

      //上一次的温度
      lazy val lastTemp: ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp", Types.of[Double]))
      //当期活动记时
      lazy val currentTimer: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer", Types.of[Long]))

      override def processElement(timeModel: TimeModel, ctx: KeyedProcessFunction[Long, TimeModel, String]#Context, out: Collector[String]): Unit = {
        //获取前一个温度
        val preTemp = lastTemp.value()
        lastTemp.update(timeModel.temp)
        //获取当前计时器
        val currentTime = currentTimer.value()
        if (preTemp == 0.0 || timeModel.temp < preTemp) {
          //温度下降,删掉当期的计时器
          ctx.timerService().deleteProcessingTimeTimer(currentTime)
          currentTimer.clear()
        } else if (timeModel.temp > preTemp && currentTime == 0) {
          //温度升高,且并没有设置计时器
          val timeTs = ctx.timerService().currentProcessingTime() + 1000
          ctx.timerService().registerProcessingTimeTimer(timeTs)
          currentTimer.update(timeTs)
        }
      }

      override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, TimeModel, String]#OnTimerContext, out: Collector[String]): Unit = {
        out.collect("Temperature " + ctx.getCurrentKey + " increased for 1 seconds")
        currentTimer.clear()
      }
    })
    keyByStream.writeAsText("/data/logs/flink/flink_temp.txt",WriteMode.OVERWRITE)
    env.execute("OnTimeTestTask")
  }
}

package com.flinkTheory.unit3

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 测试broadCastFunction<br/>
 * 参考:https://zhuanlan.zhihu.com/p/105600352
 *
 * @author chenwu on 2022.3.13
 */
//用户行为
case class UserBehavior(userId: Long, behavior: String)

//行为模式 如果前后两个行为都符合了该模式,则输出
case class BehaviorPattern(firstBehavior: String, secondBehavior: String)

object BroadCastStateFunctionTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val statebackend = new FsStateBackend("hdfs://master:9000/flink/BroadCastStateFunctionTest", false);
    env.setStateBackend(statebackend)
    env.enableCheckpointing(30000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic1 = "test"
    val topic2 = "test-flink"
    val userBehaviorStream = env.addSource(new FlinkKafkaConsumer[String](topic1, new SimpleStringSchema(), properties)).map(item => {
      val array = item.split(",")
      UserBehavior(array(0).toLong, array(1))
    }).keyBy(_.userId)
    val patternStream = env.addSource(new FlinkKafkaConsumer[String](topic2, new SimpleStringSchema(), properties)).map(item => {
      val array = item.split(",")
      BehaviorPattern(array(0), array(1))
    })
    //广播流的模式只能基于mapState
    val broadCastMapStateDescriptor = new MapStateDescriptor("behaviorPattern", classOf[Void], classOf[BehaviorPattern])
    val broadCastStream = patternStream.broadcast(broadCastMapStateDescriptor)
    val resStream = userBehaviorStream.connect(broadCastStream).process(new KeyedBroadcastProcessFunction[Long, UserBehavior, BehaviorPattern, (Long, BehaviorPattern)] {

      var lastBehavior: ValueState[UserBehavior] = _
      var broadCastDecriptor: MapStateDescriptor[Void, BehaviorPattern] = _

      override def open(parameters: Configuration): Unit = {
        val lastBehaviorDescriptor = new ValueStateDescriptor("lastBehavior", classOf[UserBehavior])
        lastBehavior = getRuntimeContext.getState(lastBehaviorDescriptor)
        broadCastDecriptor = new MapStateDescriptor("behaviorPattern", classOf[Void], classOf[BehaviorPattern])
      }

      override def processElement(value: UserBehavior, ctx: KeyedBroadcastProcessFunction[Long, UserBehavior, BehaviorPattern, (Long, BehaviorPattern)]#ReadOnlyContext, out: Collector[(Long, BehaviorPattern)]): Unit = {
        val broadCastState = ctx.getBroadcastState(broadCastMapStateDescriptor).get(null)
        val lastBehaviorValue = lastBehavior.value()
        if (broadCastState != null && lastBehaviorValue != null) {
          if (lastBehaviorValue.behavior.equals(broadCastState.firstBehavior) && value.behavior.equals(broadCastState.secondBehavior)) {
            out.collect(value.userId, broadCastState)
          }
        }
        lastBehavior.update(value)
      }

      override def processBroadcastElement(value: BehaviorPattern, ctx: KeyedBroadcastProcessFunction[Long, UserBehavior, BehaviorPattern, (Long, BehaviorPattern)]#Context, out: Collector[(Long, BehaviorPattern)]): Unit = {
        val broadCastState = ctx.getBroadcastState(broadCastMapStateDescriptor)
        broadCastState.put(null, value)
      }
    })
    resStream.print()
    env.execute("BroadCastStateFunctionTest")
  }
}

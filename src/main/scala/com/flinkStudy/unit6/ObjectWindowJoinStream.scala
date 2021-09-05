package com.flinkStudy.unit6

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 基于窗口的join
 *
 * 参考:https://blog.csdn.net/andyonlines/article/details/108173259
 *
 * @author chenwu on 2021.8.13
 */
object ObjectWindowJoinStream {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.createLocalEnvironment()
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val stream1 = env.fromElements((1, 1999l), (1, 2001l)).assignAscendingTimestamps(_._2)
    val stream2 = env.fromElements((1, 1001l), (1, 1002l), (2, 1001l), (3, 3999l)).assignAscendingTimestamps(_._2)
    stream1.join(stream2).where(_._1).equalTo(_._1).window(TumblingEventTimeWindows.of(Time.seconds(2))).apply((left, right) => {
      println(left + "=>" + right)
    })
    //输出
//    (1,1999)=>(1,1001)
//    (1,1999)=>(1,1002)
    //因为stream1 的两个元素分别属于两个窗口
    //stream2的(1,1001l),(1,1002l),(2,1001l)属于一个窗口,(3,3999l)属于另外一个窗口
    env.execute("ObjectWindowJoinStream")
  }
}

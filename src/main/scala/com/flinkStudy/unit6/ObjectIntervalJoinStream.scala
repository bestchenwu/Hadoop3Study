package com.flinkStudy.unit6

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * 基于间隔的双流join
 * 参考:https://blog.csdn.net/andyonlines/article/details/108173259
 *
 * @author chenwu on 2021.8.12
 */
object ObjectIntervalJoinStream {

  /**
   * 用户点击日志
   *
   * @param userId
   * @param eventTime
   * @param content
   */
  case class UserClickLog(userId: Long, eventTime: Long, clickContent: String)

  /**
   * 用户浏览日志
   *
   * @param userId
   * @param eventTime
   * @param browseContent
   */
  case class UserBrowseLog(userId: Long, eventTime: Long, browseContent: String)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.createLocalEnvironment()
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //将事件事件*1000 用于表示毫秒数*1000 这样1000l 与2000l  相隔1000秒(不乘以1000的话 就是1000毫秒)
    val clickStream = env.fromElements(UserClickLog(1l, 1500l, "test1"), UserClickLog(1l, 2000l, "test2")).assignAscendingTimestamps(_.eventTime * 1000).keyBy(_.userId)
    val browseStream = env.fromElements(UserBrowseLog(1l, 1000l, "test10"), UserBrowseLog(1l, 1500l, "test11"), UserBrowseLog(1L, 1501L, "test12"), UserBrowseLog(1L, 1502L, "test13")).assignAscendingTimestamps(_.eventTime * 1000).keyBy(_.userId)
    //表示两个流间距10分钟内  也就是60×10= 600秒内
    val resultStream = clickStream.intervalJoin(browseStream).between(Time.minutes(-10), Time.minutes(0)).process(new ProcessJoinFunction[UserClickLog, UserBrowseLog, String] {
      override def processElement(left: UserClickLog, right: UserBrowseLog, ctx: ProcessJoinFunction[UserClickLog, UserBrowseLog, String]#Context, out: Collector[String]): Unit =
        out.collect("left=" + left + "=>" + "right=" + right)
    })
    resultStream.print()
    env.execute("ObjectIntervalJoinStream")
  }
}

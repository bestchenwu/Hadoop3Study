package com.flinkStudy.unit1

import org.apache.flink.api.scala._

/**
 * 单词计数任务
 *
 * @author chenwu on 2021.6.6
 */
object WordCountTask {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.createRemoteEnvironment("master", 45491, "")
    val dataset = env.readTextFile("hdfs://master:9000/hadoop/temperature.txt", "UTF-8")
    dataset.flatMap(_.split(",")).map((_,1))
  }
}

package com.flinkStudy.unit1

import java.io.File

import common.constants.SymbolConstants
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode

/**
 * 单词计数任务
 *
 * @author chenwu on 2021.6.6
 */
object WordCountTask {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //val env = ExecutionEnvironment.createRemoteEnvironment("master", 34909)
    val dataset = env.readTextFile("hdfs://master:9000/hadoop/temperature.txt", "UTF-8")
    val groupDataSet = dataset.flatMap(_.split(",")).map((_, 1)).groupBy(_._1).reduce((item1, item2) => (item1._1, item1._2 + item2._2))
    groupDataSet.writeAsCsv("/data/logs/flink/wordCount.csv", SymbolConstants.SYMBOL_HH, SymbolConstants.SYMBOL_DH, WriteMode.OVERWRITE)
    env.execute("WordCountTask")
  }
}

package flinkStudy.unit1

import org.apache.flink.api.scala._

/**
 * 统计单词个数(入门)
 *
 * @author chenwu on 2021.5.15
 */
object WordCount {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    env.readTextFile("hdfs://masters")
  }
}

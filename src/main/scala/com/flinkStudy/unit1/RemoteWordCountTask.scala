package com.flinkStudy.unit1

import org.apache.flink.streaming.api.scala._

/**
 * 测试远程提交任务
 *
 * @author chenwu on 2021.11.17
 */
object RemoteWordCountTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.createLocalEnvironment(1)
    val stream = env.fromCollection(List(("aa", 1), ("bb", 2), ("aa", 3), ("cc", 4)))
    //val resStream = stream.keyBy(_._1).sum(1)
    val resStream = stream.keyBy(_._1).reduce((item1,item2)=>{
        if(item1._2<item2._2){
          item2
        }else{
          item1
        }
    })
    resStream.print()
    env.execute("RemoteWordCountTask")
  }
}

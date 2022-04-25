package com.flinkTheory.unit7

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.scala._
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction

import scala.collection.mutable.ListBuffer

/**
 * 测试全局算子状态
 */
object OperatorStateTest {

  class BufferingSink extends SinkFunction[(String,Int)] with CheckpointedFunction{

    var threashHold:Int = 0

    var bufferedList : ListBuffer[(String,Int)] = null

    var listState:ListState[(String,Int)] = null

    def apply(threshHold0:Int): Unit ={
      threashHold = threshHold0
      bufferedList = ListBuffer[(String,Int)]()
    }


    override def snapshotState(context: FunctionSnapshotContext): Unit = {
        listState.clear()
        bufferedList.foreach(item=>listState.add(item))
    }

    override def initializeState(context: FunctionInitializationContext): Unit = {
        val listDescriptor = new ListStateDescriptor[(String,Int)]("listState",createTypeInformation[(String, Int)])
        listState = context.getOperatorStateStore.getListState(listDescriptor)
        if(context.isRestored){
          val tuples = listState.get()
          if(tuples!=null){
              tuples.forEach(tuple=>bufferedList.append(tuple))
          }
        }
    }

    override def invoke(value: (String, Int), context: SinkFunction.Context[_]): Unit = {


    }
  }

  def main(args: Array[String]): Unit = {

  }
}

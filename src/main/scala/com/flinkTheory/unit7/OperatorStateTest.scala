package com.flinkTheory.unit7

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.io.FileWriter
import java.util.Properties
import scala.collection.mutable.ListBuffer

/**
 * 测试全局算子状态
 */
object OperatorStateTest {

  class BufferingSink(threshHold0: Int, logName: String) extends RichSinkFunction[(String, Int)] with CheckpointedFunction {

    var threashHold: Int = threshHold0

    val bufferedList: ListBuffer[(String, Int)] = ListBuffer[(String, Int)]()

    var listState: ListState[(String, Int)] = null

    var fileWriter: FileWriter = null

    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      fileWriter = new FileWriter(logName, true)
    }

    override def snapshotState(context: FunctionSnapshotContext): Unit = {
      listState.clear()
      bufferedList.foreach(item => {
        println("snapshotState:" + item)
        listState.add(item)
      })
    }

    override def initializeState(context: FunctionInitializationContext): Unit = {
      val listDescriptor = new ListStateDescriptor[(String, Int)]("listState", createTypeInformation[(String, Int)])
      listState = context.getOperatorStateStore.getListState(listDescriptor)
      if (context.isRestored) {
        val tuples = listState.get()
        if (tuples != null) {
          tuples.forEach(tuple => {
            println("restore tuple:" + tuple)
            bufferedList.append(tuple)
          })
        } else {
          println("restore tuple null")
        }
      }
    }

    override def invoke(value: (String, Int), context: SinkFunction.Context[_]): Unit = {
      println("value:" + value)
      bufferedList.append(value)
      if (bufferedList.size == threashHold) {
        //输出bufferedList
        bufferedList.foreach(item => fileWriter.write("invoke:" + item + "\n"))
        fileWriter.flush()
        bufferedList.clear()
      }
    }

    override def close(): Unit = {
      super.close()
      fileWriter.close()
    }
  }

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val statebackend = new FsStateBackend("hdfs://master:9000/flink/OperatorStateTest", false);
    env.setStateBackend(statebackend)
    env.enableCheckpointing(3000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "master:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val topic1 = "test"
    val userBehaviorStream = env.addSource(new FlinkKafkaConsumer[String](topic1, new SimpleStringSchema(), properties)).map(item => {
      //println("item="+item)
      val array = item.split(",")
      //println("array="+array)
      if(array.size<2){
        ("",0)
      }else{
        val item0 = array(0)
        val item1 = try {
          array(1).toInt
        } catch {
          case _: Exception => 0
        }
        (item0, item1)
      }
    }).filter(_._2 != 0)
    userBehaviorStream.addSink(new BufferingSink(5, "/data/logs/flink/OperatorStateTest.log"))
    env.execute("OperatorStateTest")
  }
}

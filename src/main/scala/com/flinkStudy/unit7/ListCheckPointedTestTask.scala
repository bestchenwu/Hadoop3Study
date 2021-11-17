package com.flinkStudy.unit7

import com.flinkStudy.unit6.TemperatureModel
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.lang.{Long => JLong}
import java.util
import java.util.{Collections, Properties}
import scala.collection.JavaConverters._
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

import java.io.{FileOutputStream, FileWriter, IOException}

/**
 * 测试{@link ListCheckpointed}算子列表状态
 *
 * @author chenwu on 2021.9.25
 */
object ListCheckPointedTestTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(15 * 1000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.107:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-list-checkpointed")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "test-flink"
    val kafkaConsumer = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val tempStream = kafkaConsumer.map(str => {
      val tempArray = str.split(",")
      TemperatureModel(tempArray(0).toLong, tempArray(1).toDouble, tempArray(2).toLong)
    })
    val resultStream = tempStream.assignAscendingTimestamps(_.ts).keyBy(_.id).flatMap(new HighTempCounter((10.0)))
    resultStream.addSink(new RichSinkFunction[(Int, JLong)] {

      private var fileWriter:FileWriter = null

      override def open(parameters: Configuration): Unit = {
          super.open(parameters)
          try{
            fileWriter = new FileWriter("/data/logs/flink/flink_temp_list_checkpointed.txt",true)
          } catch {
            case e:IOException => throw new RuntimeException(e)
          }
      }


      override def invoke(value: (Int, JLong), context: SinkFunction.Context[_]): Unit = {
          val newLine = s"subTaskIndex:${value._1},highTempCount:${value._2}\n"
          fileWriter.write(newLine)
      }

      override def close(): Unit = {
          fileWriter.close()
      }
    })

    env.execute("ListCheckPointedTestTask")
  }
}

/**
 * 高温计数器
 *
 * @param threshHold 高温阀值
 * @author chenwu on 2021.9.25
 */
class HighTempCounter(val threshHold: Double) extends RichFlatMapFunction[TemperatureModel, (Int, JLong)] with ListCheckpointed[JLong] {

  private var highTempCount = 0
  //子任务的索引号
  private lazy val subTaskIdx = getRuntimeContext.getIndexOfThisSubtask

  override def flatMap(temperatureModel: TemperatureModel, out: Collector[(Int, JLong)]): Unit = {
    if (temperatureModel.temp > threshHold) {
      highTempCount += 1
      out.collect((subTaskIdx, highTempCount))
    }
  }

  override def snapshotState(checkpointId: Long, timestamp: Long): util.List[JLong] = {
    Collections.singletonList(highTempCount)
  }

  override def restoreState(state: util.List[JLong]): Unit = {
    highTempCount = 0
    for (temp <- state.asScala) {
      highTempCount = highTempCount+temp.toInt
    }
  }

}

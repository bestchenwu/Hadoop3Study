package com.flinkStudy.unit6

import java.util.Properties

import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink

/**
 * 使用自定义的平均温度任务
 *
 * @author chenwu on 2021.8.1
 */
object AvgTempertureTask {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(15 * 1000l)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.2.107:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-flink")
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val topic = "test-temp"
    val kafkaConsumer = env.addSource(new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties))
    val tempStream = kafkaConsumer.map(str => {
      val tempArray = str.split(",")
      TemperatureModel(tempArray(0).toLong, tempArray(1).toDouble, tempArray(2).toLong)
    })
    val timeStream = tempStream.assignAscendingTimestamps(_.ts).map(item => (item.id, item.temp)).keyBy(_._1).timeWindow(Time.seconds(1l))
    val StreamFileSinkFunction = StreamingFileSink.forRowFormat(new Path("/data/logs/flink/flink_temp_avg.txt"), new SimpleStringEncoder[(Long,Double)]("UTF-8")).build()
    timeStream.aggregate(new AvgTempertureFunction).addSink(StreamFileSinkFunction)
    env.execute("AvgTempertureTask")
  }
}

/**
 * 温度平均函数
 *
 * @author chenwu on 2021.8.1
 */
class AvgTempertureFunction extends AggregateFunction[(Long, Double), (Long, Double, Int), (Long, Double)] {
  override def createAccumulator(): (Long, Double, Int) = (0, 0.0, 0)

  override def add(value: (Long, Double), accumulator: (Long, Double, Int)): (Long, Double, Int) = {
    (value._1, value._2 + accumulator._2, 1 + accumulator._3)
  }

  override def getResult(accumulator: (Long, Double, Int)): (Long, Double) = {
    (accumulator._1, accumulator._2)
  }

  override def merge(a: (Long, Double, Int), b: (Long, Double, Int)): (Long, Double, Int) = {
    (a._1, a._2 + b._2, a._3 + b._3)
  }
}

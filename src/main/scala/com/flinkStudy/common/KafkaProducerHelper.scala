package com.flinkStudy.common

import com.flinkStudy.unit6.TemperatureModel
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.time.{LocalDateTime, ZoneOffset}
import java.util.Properties
import scala.util.Random
import java.util.{Random => JRandom}
/**
 * kafka 生产者的工具类
 * 用于构造kafka消息
 *
 * @author chenwu on 2021.8.13
 */
object KafkaProducerHelper {

  def main(args: Array[String]): Unit = {
    val properties = new Properties()
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"Master:9092")
    val kafkaProducer = new KafkaProducer[String, String](properties)
    val topic = "test-flink"
    val value = "coProcess"
    val random = new JRandom(47l)
    while (true) {
//      val time = System.currentTimeMillis().toString
//      val newValue = value+time
//      val record = new ProducerRecord[String, String](topic, newValue + "," + time)
      val id = random.nextInt(5)
      val temp = random.nextInt(20).toDouble
      val localTime = LocalDateTime.now()
      //转换为毫秒数
      val time = localTime.toInstant(ZoneOffset.of("+8")).toEpochMilli
      kafkaProducer.send(new ProducerRecord[String,String](topic,s"${id},${temp},${time}"))
      try {
        Thread.sleep(2 * 1000)
      } catch {
        case e:InterruptedException => e.printStackTrace()
      }
    }
  }
}

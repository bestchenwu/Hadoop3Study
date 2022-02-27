package com.flinkStudy.common

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.time.{LocalDateTime, LocalTime, ZoneOffset}
import java.util.Properties
import scala.util.Random

/**
 * kafka发出两种时间线(一个正常发送,一个发送过去的时间)的工具类
 *
 * @author chenwu on 2021.9.12
 */
object KafkaSendTwoTimeLineHeper {

  def main(args: Array[String]): Unit = {
    val properties = new Properties()
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "Master:9092")
    val kafkaProducer = new KafkaProducer[String, String](properties)
    val topic = "test-flink"
    val keyList = List("key1", "key2", "key3")
    val random = new Random(47)
    while (true) {
      val index = random.nextInt(keyList.size)
      val key = keyList(index)
      val localTime = LocalDateTime.now()
      //转换为毫秒数
      val time = localTime.toInstant(ZoneOffset.of("+8")).toEpochMilli
      //发送正常时间
      kafkaProducer.send(new ProducerRecord[String, String](topic, key + "," + time))
      //发送过去时间
      val lastTime = localTime.minusSeconds(3l).toInstant(ZoneOffset.of("+8")).toEpochMilli
      kafkaProducer.send(new ProducerRecord[String, String](topic, key + "," + lastTime))
      try {
        Thread.sleep(1000)
      } catch {
        case e:InterruptedException => e.printStackTrace()
      }
    }
  }
}

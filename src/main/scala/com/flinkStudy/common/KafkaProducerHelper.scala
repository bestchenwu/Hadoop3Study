package com.flinkStudy.common

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

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
    while (true) {
      val time = System.currentTimeMillis().toString
      val newValue = value+time
      val record = new ProducerRecord[String, String](topic, newValue + "," + time)
      kafkaProducer.send(record)
      try {
        Thread.sleep(3 * 1000)
      } catch {
        case e => e.printStackTrace()
      }
    }
  }
}

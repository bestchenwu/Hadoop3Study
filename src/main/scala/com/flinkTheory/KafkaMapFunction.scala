package com.flinkTheory

import org.apache.flink.api.common.functions.MapFunction

class KafkaMapFunction extends MapFunction[String,(String,Int)]{
  override def map(value: String): (String, Int) = {
    val array = value.split(",")
    (array(0),array(1).toInt)
  }
}

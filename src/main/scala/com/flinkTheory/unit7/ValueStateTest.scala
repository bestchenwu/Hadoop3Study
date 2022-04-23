package com.flinkTheory.unit7

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.typeinfo.{TypeHint, TypeInformation}
import org.apache.flink.api.java.typeutils.TupleTypeInfo
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

class RichCountAggregate extends RichFlatMapFunction[(Long, Long), (Long, Long)] {

  var sumState: ValueState[(Long, Long)] = null

  override def open(parameters: Configuration): Unit = {
    sumState = getRuntimeContext.getState(new ValueStateDescriptor("sumState", Types.TUPLE[(Long, Long)]))
  }

  override def flatMap(value: (Long, Long), out: Collector[(Long, Long)]): Unit = {
    val tuple = sumState.value()
    if (tuple == null) {
      sumState.update((1, value._2))
    } else {
      sumState.update((tuple._1 + 1, tuple._2 + value._2))
    }
    if (sumState.value()._1 % 2 == 0) {
      out.collect(sumState.value())
      sumState.clear()
    }
  }
}

object ValueStateTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.createLocalEnvironment()
    val elements = env.fromElements((1l, 2l), (1l, 5l), (1l, 7l), (1l, 4l), (1l, 2l))
    val res = elements.keyBy(_._1).flatMap(new RichCountAggregate)
    res.print()
    env.execute("ValueStateTest")
  }
}

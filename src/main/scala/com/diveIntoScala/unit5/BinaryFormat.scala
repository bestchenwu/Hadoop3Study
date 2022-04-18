package com.diveIntoScala.unit5

/**
 * 实现把对象序列化的工具
 *
 * @tparam T
 */
trait BinaryFormat[T] {

  def asBinary(entity: T): Array[Byte]
}

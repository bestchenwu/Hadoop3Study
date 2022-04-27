package com.diveIntoScala.unit6
import scala.collection.mutable.Map
trait Observable {
  type Handle
  val callbacks : Map[Handle,Function1[this.type ,Unit]] = Map()
  def observe(callback:this.type =>Unit):Handle = {
      val handle = createHandler(callback)
      callbacks+=(handle->callback)
      handle
  }

  protected def createHandler(callback:this.type =>Unit):Handle
}

package com.evolutiongaming.metric


/**
  * Typeclass to encode T into String
  * @tparam T
  */
trait LabelEncoder[T] {
  def encode(t: T): String
}
object LabelEncoder {
  implicit val stringInstance: LabelEncoder[String] = new LabelEncoder[String] {
    override def encode(t: String): String = t
  }
}
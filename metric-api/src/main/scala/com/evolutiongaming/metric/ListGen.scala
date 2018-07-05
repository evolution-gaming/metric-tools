package com.evolutiongaming.metric

import shapeless.{::, HList, HNil}


/**
  * Typeclass to generate List[String] from T
  * @tparam T
  */
trait ListGen[T] {
  def gen(t: T): List[String]
}
object ListGen {
  implicit val hnilGen: ListGen[HNil] = new ListGen[HNil] {
    override def gen(t: HNil): List[String] = Nil
  }

  implicit def hlistGen[H, T <: HList](
    implicit
    hEncoder: LabelEncoder[H],
    tEncoder: ListGen[T]
  ): ListGen[H :: T] = new ListGen[H :: T] {
    override def gen(t: ::[H, T]): List[String] = t match {
      case h :: t => hEncoder.encode(h) :: tEncoder.gen(t)
    }
  }
}

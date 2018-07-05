package com.evolutiongaming.metric

import shapeless._

/**
  * Typeclass for conversion T into HList
  * with stringInstance, unitInstance as Generic.Aux does not provide that
  * @tparam L
  * @tparam G
  */
trait LabelGen[L, G <: HList] {
  def gen(l: L): G
}

object LabelGen {

  implicit val unitInstance: LabelGen[Unit, HNil] = new LabelGen[Unit, HNil] {
    override def gen(l: Unit): HNil = HNil
  }

  implicit val hNilInstance: LabelGen[HNil, HNil] = new LabelGen[HNil, HNil] {
    override def gen(l: HNil): HNil = l
  }

  implicit val stringInstance: LabelGen[String, String :: HNil] = new LabelGen[String, String :: HNil] {
    override def gen(l: String): ::[String, HNil] = l :: HNil
  }

  implicit def productLabel[P <: Product, H <: HList](
    implicit g: Generic.Aux[P, H]
  ): LabelGen[P, H] = new LabelGen[P, H] {
    override def gen(l: P): H = g.to(l)
  }
}
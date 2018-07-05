package com.evolutiongaming.metric

import shapeless._
import shapeless.ops.hlist

/**
  * Provides abstract interface to metrics
  * Typeclass derivations works as follows:
  *
  * For any Labels P, and Label names N, generic representations are derived using LableGen typeclass,
  * Generic represenations are aligned using hlist.Align
  * Generic representations are converted to List[String] using ListGen and LabelEncoder typeclasses
  *
  * 1. Type P - label, should have LabelGen type class instance
  * 2. generic derivation: P -> LabelGen -> HList, generic derivation done for both labels and label names
  * 3. Both Generic repr's are hlist.Align'ed, compilation will fail on mismatch
  * 4. Label list generation: Generic repr -> ListGen & LabelEncoder -> List[String],
  * default LabelEncoder is provided only for String
  */
trait Metric {

  trait Counter[N, H <: HList] {
    def inc[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit
    def inc[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit
  }

  trait Gauge[N, H <: HList] extends Counter[N, H] {
    def dec[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit
    def set[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit
  }

  trait Histogram[N, H <: HList] {
    trait Timer {
      def stop(): Double
    }

    def startTimer[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Timer

    def observed[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit
  }

  def counter[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Counter[N, H]

  def gauge[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Gauge[N, H]

  def histogram[N, H <: HList](name: String, help: String, labelNames: N, buckets: List[Double])(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Histogram[N, H]
}

package com.evolutiongaming.metric

import shapeless.HList
import shapeless.ops.hlist

/**
  * No operation implementation, meant to be used as a way to disable metrics
  */
class NoOpMetric extends Metric {
  override def counter[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]): Counter[N, H] =
    new Counter[N, H] {
      override def inc[P, L <: HList](labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = {}

      override def inc[P, L <: HList](i: Double, labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = {}
    }

  override def gauge[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]): Gauge[N, H] =
    new Gauge[N, H] {
      override def dec[P, L <: HList](labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = ()

      override def set[P, L <: HList](i: Double, labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = ()

      override def inc[P, L <: HList](labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = ()

      override def inc[P, L <: HList](i: Double, labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = ()
    }

  override def histogram[N, H <: HList](name: String, help: String, labelNames: N, buckets: List[Double])(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]): Histogram[N, H] =
    new Histogram[N, H] {
      override def startTimer[P, L <: HList](labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Timer = {
        val clock = System.currentTimeMillis()
        new Timer {
          override def stop(): Double = (System.currentTimeMillis() - clock).toDouble / 1000
        }
      }

      override def observed[P, L <: HList](i: Double, labels: P)(
        implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
      ): Unit = ()
    }
}

object NoOpMetric {
  def apply: NoOpMetric = new NoOpMetric()
}
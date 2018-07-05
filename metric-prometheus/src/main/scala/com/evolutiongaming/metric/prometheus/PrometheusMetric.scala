package com.evolutiongaming.metric.prometheus

import com.evolutiongaming.metric.{LabelGen, ListGen, Metric}
import io.prometheus.client.{CollectorRegistry, Counter => PCounter, Gauge => PGauge, Histogram => PHistogram}
import shapeless.HList
import shapeless.ops.hlist

class PrometheusMetric(registry: CollectorRegistry) extends Metric {

  def report: Report = Report(registry)

  class PrometheusCounter[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ) extends Counter[N, H] {

    private val underlying = PCounter.build(name, help).labelNames(NL.gen(lgen.gen(labelNames)): _*).create()
    registry.register(underlying)

    override def inc[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).inc()

    override def inc[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).inc(i)
  }

  class PrometheusGauge[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ) extends Gauge[N, H] {

    private val underlying = PGauge.build(name, help).labelNames(NL.gen(lgen.gen(labelNames)): _*).create()
    registry.register(underlying)

    override def dec[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).dec()

    override def set[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).set(i)

    override def inc[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).inc()

    override def inc[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).inc(i)
  }

  class PrometheusHistogram[N, H <: HList](name: String, help: String, labelNames: N, buckets: List[Double])(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ) extends Histogram[N, H] {

    private val underlying = PHistogram.build(name, help)
      .labelNames(NL.gen(lgen.gen(labelNames)): _*)
      .buckets(buckets: _*)
      .create()
    registry.register(underlying)

    override def startTimer[P, L <: HList](labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Timer =
      new Timer {
        private val timer = underlying.labels(LL.gen(ngen.gen(labels)): _*).startTimer()

        override def stop(): Double = timer.observeDuration()
      }

    override def observed[P, L <: HList](i: Double, labels: P)(
      implicit ngen: LabelGen[P, L], align: hlist.Align[H, L], LL: ListGen[L]
    ): Unit =
      underlying.labels(LL.gen(ngen.gen(labels)): _*).observe(i)
  }

  override def counter[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Counter[N, H] =
    new PrometheusCounter[N, H](name, help, labelNames)

  override def gauge[N, H <: HList](name: String, help: String, labelNames: N)(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Gauge[N, H] =
    new PrometheusGauge[N, H](name, help, labelNames)

  override def histogram[N, H <: HList](name: String, help: String, labelNames: N, buckets: List[Double])(
    implicit lgen: LabelGen[N, H], NL: ListGen[H]
  ): Histogram[N, H] =
    new PrometheusHistogram[N, H](name, help, labelNames, buckets)
}

object PrometheusMetric {
  def apply(): PrometheusMetric =
    apply(CollectorRegistry.defaultRegistry)

  def apply(registry: CollectorRegistry): PrometheusMetric =
    new PrometheusMetric(StandardPrometheusMetricCollectors(registry))
}
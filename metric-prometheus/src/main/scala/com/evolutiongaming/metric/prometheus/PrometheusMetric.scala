package com.evolutiongaming.metric.prometheus

import com.evolutiongaming.metric.Metric
import io.prometheus.client.{Collector, CollectorRegistry, Counter => PCounter, Gauge => PGauge, Histogram => PHistogram}

class PrometheusMetric(registry: CollectorRegistry) extends Metric {

  def report: Report = Report(registry)

  override def counter(name: String, help: String, labelNames: Seq[String]) = {
    val underlying = PCounter.build(name, help).labelNames(labelNames: _*).create()
    registry.register(underlying)
    new PrometheusCounter(Seq.empty, underlying)
  }

  override def gauge(name: String, help: String, labelNames: Seq[String]) = {
    val underlying = PGauge.build(name, help).labelNames(labelNames: _*).create()
    registry.register(underlying)
    new PrometheusGauge(Seq.empty, underlying)
  }

  override def histogram(name: String, help: String, labelNames: Seq[String], buckets: Vector[Double]) = {
    val underlying = PHistogram.build(name, help)
      .labelNames(labelNames: _*)
      .buckets(buckets: _*)
      .create()
    registry.register(underlying)
    new PrometheusHistogram(Seq.empty, underlying)
  }

  def register(collector: Collector): Unit = registry.register(collector)

  class PrometheusCounter(labels: Seq[String], counter: PCounter) extends Counter {
    override def labels(labels: Seq[String]): Counter = new PrometheusCounter(labels, counter)
    override def inc(): Unit = counter.labels(labels: _*).inc()
    override def inc(i: Double): Unit = counter.labels(labels: _*).inc(i)
  }

  class PrometheusGauge(labels: Seq[String], gauge: PGauge) extends Gauge {
    override def labels(labels: Seq[String]): Gauge = new PrometheusGauge(labels, gauge)
    override def inc(): Unit = gauge.labels(labels: _*).inc()
    override def dec(): Unit = gauge.labels(labels: _*).dec()
    override def set(i: Double): Unit = gauge.labels(labels: _*).set(i)
  }

  class PrometheusHistogram(labels: Seq[String], histogram: PHistogram) extends Histogram {
    override def startTimer: Timer = new Timer {
      private val timer = histogram.labels(labels: _*).startTimer()
      override def stop(): Double = timer.observeDuration()
    }
    override def observed(dur: Double): Unit = histogram.labels(labels: _*).observe(dur)
    override def labels(labels: Seq[String]): Histogram = new PrometheusHistogram(labels, histogram)
  }
}

object PrometheusMetric {
  def apply(): PrometheusMetric =
    apply(CollectorRegistry.defaultRegistry)

  def apply(registry: CollectorRegistry): PrometheusMetric =
    new PrometheusMetric(StandardPrometheusMetricCollectors(registry))
}
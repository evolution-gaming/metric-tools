package com.evolutiongaming.metric

class NoOpMetric extends Metric {
  override def counter(name: String, help: String, labelNames: Seq[String]) = new Counter {
    override def inc(): Unit = {}
    override def labels(labels: Seq[String]): Counter = this
    override def inc(i: Double): Unit = {}
  }
  override def gauge(name: String, help: String, labelNames: Seq[String]) = new Gauge {
    override def dec(): Unit = {}
    override def inc(): Unit = {}
    override def labels(labels: Seq[String]): Gauge = this
    override def set(i: Double): Unit = {}
  }
  override def histogram(name: String, help: String, labelNames: Seq[String], buckets: Vector[Double]) = new Histogram {
    override def observed(dur: Double): Unit = {}
    override def startTimer: Timer = new Timer {
      override def stop(): Double = 0.0
    }
    override def labels(labels: Seq[String]): Histogram = this
  }
}

object NoOpMetric {
  def apply: NoOpMetric = new NoOpMetric()
}
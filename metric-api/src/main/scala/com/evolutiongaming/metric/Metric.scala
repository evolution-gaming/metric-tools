package com.evolutiongaming.metric

/**
  * Provides abstract interface to metrics
  */
trait Metric {
  trait Labeled[+T] {
    def labels(labels: Seq[String]): T
  }

  trait Counter extends Labeled[Counter] {
    def inc(): Unit
    def inc(i: Double): Unit
  }

  trait Gauge extends Labeled[Gauge] {
    def inc(): Unit
    def dec(): Unit
    def set(i: Double): Unit
  }

  trait Histogram extends Labeled[Histogram] {
    trait Timer {
      def stop(): Double
    }

    def startTimer: Timer
    def observed(dur: Double): Unit
  }

  def counter(name: String, help: String, labelNames: Seq[String]): Counter
  def gauge(name: String, help: String, labelNames: Seq[String]): Gauge
  def histogram(name: String, help: String, labelNames: Seq[String], buckets: Vector[Double]): Histogram
}
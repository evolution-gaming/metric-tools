package com.evolutiongaming.metric.prometheus

import io.prometheus.client.CollectorRegistry
import org.scalatest.{FlatSpec, Matchers}

class PrometheusMetricSpec extends FlatSpec with Matchers {

  it should "report recorded metrics" in {
    val metrics = new PrometheusMetric(new CollectorRegistry())
    val g = metrics.gauge("test", "provides test gauge", Seq.empty)
    g.set(6.6)
    metrics.report.text should contain
      """
        |# HELP test provides test gauge
        |# TYPE test gauge
        |test 6.6
      """.stripMargin
  }

}

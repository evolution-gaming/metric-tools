package com.evolutiongaming.metric.prometheus

import io.prometheus.client.CollectorRegistry
import org.scalatest._

class PrometheusMetricSpec extends fixture.FlatSpec with Matchers {

  override type FixtureParam = PrometheusMetric

  override def withFixture(test: OneArgTest): Outcome = {
    test(new PrometheusMetric(new CollectorRegistry()))
  }

  it should "report recorded metrics" in { metrics =>
    val g = metrics.gauge("test", "provides test gauge", ())
    g.set(6.6, ())
    metrics.report.text should include("""test 6.6""")
  }

  it should "record histogram with labels" in { metrics =>
    val hist = metrics.histogram("test", "provides test histogram", "test_label", List(1.0, 2.0, 5.0, 10.0))
    hist.observed(4.5, "xxx")

    metrics.report.text should include(
      """
        |# HELP test provides test histogram
        |# TYPE test histogram
        |test_bucket{test_label="xxx",le="1.0",} 0.0
        |test_bucket{test_label="xxx",le="2.0",} 0.0
        |test_bucket{test_label="xxx",le="5.0",} 1.0
        |test_bucket{test_label="xxx",le="10.0",} 1.0
        |test_bucket{test_label="xxx",le="+Inf",} 1.0
        |test_count{test_label="xxx",} 1.0
        |test_sum{test_label="xxx",} 4.5
      """.stripMargin.trim)
  }

  it should "record counter" in { metrics =>
    val counter = metrics.counter("test_c", "provides test counter", ("label_name1", "label_name"))
    counter.inc(1.0, ("label1", "label2"))
    metrics.report.text should include("""test_c{label_name1="label1",label_name="label2",} 1.0""")
  }

  it should "record standard jvm metric" in { _ =>
    val metrics = PrometheusMetric()
    metrics.report.text should (
      include("jvm_threads_")
        and include("process_cpu_")
        and include("process_open_")
        and include("jvm_buffer_pool_")
        and include("jvm_memory_")
        and include("jvm_gc_")
        and include("jvm_classes_")
      )
  }

}

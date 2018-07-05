package com.evolutiongaming.metric

import org.scalatest.{FlatSpec, Matchers}

class NoopMetricSpec extends FlatSpec with Matchers {
  it should "compile when names and labels matches" in {
    """
      |val m: Metric = new NoOpMetric
      |    m.counter("test", "this is test counter", "the_label_name")
      |      .inc("the_label")
    """.stripMargin should compile

    """
      |val m: Metric = new NoOpMetric
      |    m.counter("test", "this is test counter", ("the_label_name", "other_label"))
      |      .inc(("the_label", "other_label"))
    """.stripMargin should compile
  }

  it should "fail compilation on label/name mismatch" in {
    """
      |val m: Metric = new NoOpMetric
      |    m.counter("test", "this is test counter", ("the_label_name", "other_label"))
      |      .inc(("the_label"))
    """.stripMargin shouldNot compile
  }
}

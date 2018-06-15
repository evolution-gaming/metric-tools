package com.evolutiongaming.metric.prometheus

import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.prometheus.client.CollectorRegistry
import org.scalatest.{FlatSpec, Matchers}

class AkkaHttpPrometheusExporterRouteSpec extends FlatSpec with Matchers with ScalatestRouteTest{
  it should "expose reported metrics in" in {
    val metrics = new PrometheusMetric(new CollectorRegistry())
    val exporter = AkkaHttpPrometheusExporterRoute(metrics)
    val cnt = metrics.counter("test", "provides test counter", Seq.empty)
    cnt.inc()
    cnt.inc()

    Get(s"/${AkkaHttpPrometheusExporterRoute.DefaultConfiguration.uri}") ~> exporter ~> check {
      responseAs[String] should contain
        """
          |# HELP test provides test counter
          |# TYPE test counter
          |test 2.0
        """.stripMargin
    }
  }
}

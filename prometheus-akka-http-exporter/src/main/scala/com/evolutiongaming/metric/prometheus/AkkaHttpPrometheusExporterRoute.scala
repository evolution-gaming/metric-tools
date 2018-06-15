package com.evolutiongaming.metric.prometheus

import akka.http.scaladsl.model.{ContentType, HttpEntity}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.prometheus.client.exporter.common.TextFormat


object AkkaHttpPrometheusExporterRoute {
  private val ResponseContentType = ContentType.parse(TextFormat.CONTENT_TYPE_004).right.getOrElse(sys.error(
    s"Invalid content type in Prometheus client: '${ TextFormat.CONTENT_TYPE_004 }'"))

  val DefaultConfiguration: Configuration = Configuration("metrics")

  case class Configuration(uri: String)

  def apply(metrics: PrometheusMetric): Route = apply(metrics, DefaultConfiguration)

  def apply(metrics: PrometheusMetric, config: Configuration): Route =
    ( get & path(config.uri) ) { complete(HttpEntity(ResponseContentType, metrics.report.text.getBytes)) }
}
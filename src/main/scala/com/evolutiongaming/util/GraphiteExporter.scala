package com.evolutiongaming.util

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, MetricRegistry}
import com.typesafe.config.ConfigFactory

object GraphiteExporter {
  private lazy val config = ConfigFactory.load

  def export(domain: String, registry: MetricRegistry): MetricRegistry = {
    startGraphiteExporter(domain, registry)
    registry
  }

  def startGraphiteExporter(domain: String, registry: MetricRegistry): Option[GraphiteReporter] = {
    val host = config.getString("graphite.host")
    if (!host.isEmpty) {
      val node = config.getString("graphite.node")
      val port = config.getInt("graphite.port")
      val graphite = new Graphite(new InetSocketAddress(host, port))
      val reporter = GraphiteReporter.forRegistry(registry)
        .prefixedWith(node + "." + domain)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite)
      reporter.start(1, TimeUnit.MINUTES)
      Some(reporter)
    } else None
  }
}
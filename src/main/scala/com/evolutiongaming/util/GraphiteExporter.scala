package com.evolutiongaming.util

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, MetricRegistry}
import com.typesafe.config.Config

object GraphiteExporter {

  def export(domain: String, registry: MetricRegistry, config: Config): MetricRegistry = {
    startGraphiteExporter(domain, registry, config)
    registry
  }

  def startGraphiteExporter(
    domain: String,
    registry: MetricRegistry,
    config: Config
  ): Option[GraphiteReporter] = {

    if (config.hasPath("graphite.host")) {
      val host = config.getString("graphite.host")
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
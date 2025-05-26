package com.evolutiongaming.util

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, MetricRegistry}
import com.typesafe.config.{Config => TypesafeConfig}
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

object GraphiteExporter {

  def `export`(domain: String, registry: MetricRegistry, config: TypesafeConfig): MetricRegistry = {
    startGraphiteExporter(domain, registry, config)
    registry
  }

  def startGraphiteExporter(
    domain: String,
    registry: MetricRegistry,
    config: TypesafeConfig,
  ): Option[GraphiteReporter] = {
    val config1 = ConfigSource
      .fromConfig(config)
      .at("graphite")
      .load[Config]
    for {
      config <- config1.toOption if config.host.nonEmpty
    } yield startGraphiteExporter(domain, registry, config)
  }

  def startGraphiteExporter(
    domain: String,
    registry: MetricRegistry,
    config: Config,
  ): GraphiteReporter = {
    val graphite = new Graphite(new InetSocketAddress(config.host, config.port))
    val reporter = GraphiteReporter
      .forRegistry(registry)
      .prefixedWith(s"${ config.node }.$domain")
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .filter(MetricFilter.ALL)
      .build(graphite)
    reporter.start(1, TimeUnit.MINUTES)
    reporter
  }

  final case class Config(host: String, node: String, port: Int = 2003)

  object Config {
    implicit val ConfigConfigReader: ConfigReader[Config] = deriveReader[Config]
  }
}

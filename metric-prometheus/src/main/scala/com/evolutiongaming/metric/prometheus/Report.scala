package com.evolutiongaming.metric.prometheus

import java.io.CharArrayWriter
import java.{util => ju}

import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.logback.InstrumentedAppender.{COUNTER_NAME => LogCounterName}
import io.prometheus.client.{Collector, CollectorRegistry}

import scala.collection.JavaConverters._

case class Report(text: String)

private[prometheus] object Report {
  private val InitSize = 16 * 1024

  private def metricFamilySamples(registry: CollectorRegistry): ju.Enumeration[Collector.MetricFamilySamples] = {
    val combined =
      registry.metricFamilySamples.asScala ++
        CollectorRegistry.defaultRegistry.metricFamilySamples.asScala.collect {
          case samples if samples.name == LogCounterName => samples
        }
    combined.asJavaEnumeration
  }

  def apply(cs: CollectorRegistry): Report = {
    val writer = new CharArrayWriter(InitSize)
    TextFormat.write004(writer, metricFamilySamples(cs))
    Report(writer.toString)
  }
}
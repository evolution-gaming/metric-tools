package com.evolutiongaming.metric.prometheus

import io.prometheus.client._
import io.prometheus.client.hotspot._

private[prometheus] object StandardPrometheusMetricCollectors {
  def apply(registry: CollectorRegistry): CollectorRegistry = {
    val exports: Seq[Collector] = Seq(
      new StandardExports,
      new MemoryPoolsExports,
      new GarbageCollectorExports,
      new ThreadExports,
      new ClassLoadingExports,
      new VersionInfoExports,
      new BufferPoolsExports
    )

    exports foreach registry.register

    registry
  }
}

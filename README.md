# metric-tools [![Build Status](https://travis-ci.org/evolution-gaming/metric-tools.svg)](https://travis-ci.org/evolution-gaming/metric-tools) [![Coverage Status](https://coveralls.io/repos/evolution-gaming/metric-tools/badge.svg)](https://coveralls.io/r/evolution-gaming/metric-tools) [ ![version](https://api.bintray.com/packages/evolutiongaming/maven/metric-tools/images/download.svg) ](https://bintray.com/evolutiongaming/maven/metric-tools/_latestVersion)

# Abstract Metric interface with Prometheus implementation

The library provides:

1. `metric-api` abstract metric interface and NoOp implementation
1. `metric-prometheus` prometheus implementations
1. `prometheus-akka-http-exporter` provides akka-http route for metric exposure

## Setup

sbt
```scala
resolvers += Resolver.bintrayRepo("evolutiongaming", "maven")
libraryDependencies ++= Seq(
"com.evolutiongaming" %% "metric-api",
"com.evolutiongaming" %% "metric-prometheus",
"com.evolutiongaming" %% "prometheus-akka-http-exporter").map(_ % "1.2")
```

## Quick Start

Using metrics

```scala
val metrics: Metric = PrometheusMetric() //it will be created on default metric registry and will register all jvm collectors 
//when no labels needed provide Unit.
val g = metrics.gauge("test", "provides test gauge", ())
g.set(6.6, ())

// any product with strings can be provided as labels and names
// to support other types, LabelEncoder implicit instance should be in scope 
val counter = metrics.counter("test_c", "provides test counter", ("label_name1", "label_name"))
counter.inc(1.0, ("label1", "label2"))
....

val metricRoute = AkkaHttpPrometheusExporterRoute(metrics) //by default metrics are exposed on /metrics, but can be overriden by passing Configuration

val completeRoute = yourBusinessRoute ~ metricRoute

val bindingFuture = Http().bindAndHandle(completeRoute, "localhost", 8080)
```

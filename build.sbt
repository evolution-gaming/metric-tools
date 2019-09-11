name := "metric-tools"

organization := "com.evolutiongaming"

homepage := Some(new URL("http://github.com/evolution-gaming/metric-tools"))

startYear := Some(2017)

organizationName := "Evolution Gaming"

organizationHomepage := Some(url("http://evolutiongaming.com"))

bintrayOrganization := Some("evolutiongaming")

scalaVersion := crossScalaVersions.value.head

crossScalaVersions := Seq("2.13.0", "2.12.10")

releaseCrossBuild := true

resolvers += Resolver.bintrayRepo("evolutiongaming", "maven")

scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-no-link-warnings")

val metricsVersion = "3.2.6"

libraryDependencies ++= Seq(
  "io.dropwizard.metrics"  % "metrics-core"     % metricsVersion,
  "io.dropwizard.metrics"  % "metrics-graphite" % metricsVersion,
  "com.typesafe"           % "config"           % "1.3.4",
  "com.github.pureconfig" %% "pureconfig"       % "0.11.1",
  "com.evolutiongaming"   %% "executor-tools"   % "1.0.2")

licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")))

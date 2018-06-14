

val metricsVersion = "3.2.5"

val commonSettings = Seq(
  name := "metric-tools",
  organization := "com.evolutiongaming",
  homepage := Some(new URL("http://github.com/evolution-gaming/metric-tools")),
  startYear := Some(2017),
  organizationName := "Evolution Gaming",
  organizationHomepage := Some(url("http://evolutiongaming.com")),
  bintrayOrganization := Some("evolutiongaming"),
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.12.4", "2.11.12"),
  releaseCrossBuild := true,
  resolvers += Resolver.bintrayRepo("evolutiongaming", "maven"),
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Xfuture"
  ),
  scalacOptions in(Compile, doc) ++= Seq("-groups", "-implicits", "-no-link-warnings"),
  licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")))
)

lazy val `metric-tools` = (project in file("metric-tools"))
  .settings(commonSettings)
  .settings(Seq(
    name := "metric-tools",
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-core" % metricsVersion,
      "io.dropwizard.metrics" % "metrics-graphite" % metricsVersion,
      "com.typesafe" % "config" % "1.3.2",
      "com.evolutiongaming" %% "executor-tools" % "1.0.0"),
  ))
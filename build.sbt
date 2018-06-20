import Dependencies._

val commonSettings = Seq(
  organization := "com.evolutiongaming",
  homepage := Some(new URL("http://github.com/evolution-gaming/metric-tools")),
  startYear := Some(2017),
  organizationName := "Evolution Gaming",
  organizationHomepage := Some(url("http://evolutiongaming.com")),
  bintrayOrganization := Some("evolutiongaming"),
  scalaVersion := "2.12.6",
  crossScalaVersions := Seq("2.12.6", "2.11.12"),
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
  licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),
  libraryDependencies ++= Config :: ScalaTest :: Nil
)

lazy val `metric-tools` = (project in file("metric-tools"))
  .settings(commonSettings)
  .settings(Seq(
    name := "metric-tools",
    libraryDependencies ++= DropWizard :+ ExecutorTools
  ))

lazy val `metric-api` = (project in file("metric-api"))
  .settings(commonSettings)
  .settings(Seq(
    name := "metric-api"
  ))

lazy val `metric-prometheus` = (project in file("metric-prometheus"))
  .settings(commonSettings)
  .settings(Seq(
    name := "metric-prometheus",
    libraryDependencies ++= Prometheus
  )).dependsOn(`metric-api`)

lazy val `prometheus-akka-http-exporter` = (project in file("prometheus-akka-http-exporter"))
  .settings(commonSettings)
  .settings(
    Seq(
      name := "prometheus-akka-http-exporter",
      libraryDependencies ++= AkkaHttp :+ AkkaActor :+ AkkaStreams
    )
  ).dependsOn(`metric-prometheus`)

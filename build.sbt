
val commonSettings = Seq(
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
  licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.3.2",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
)

lazy val `metric-tools` = (project in file("metric-tools"))
  .settings(commonSettings)
  .settings(Seq(
    name := "metric-tools",
    libraryDependencies ++= Seq(
      "com.evolutiongaming" %% "executor-tools" % "1.0.0")
      ++ Seq(
      "io.dropwizard.metrics" % "metrics-core",
      "io.dropwizard.metrics" % "metrics-graphite",
    ).map(_ % "3.2.5"),
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
    libraryDependencies ++= Seq(
      "io.prometheus" % "simpleclient_common",
      "io.prometheus" % "simpleclient_hotspot",
      "io.prometheus" % "simpleclient_logback"
    ).map(_ % "0.3.0")
  )).dependsOn(`metric-api`)


lazy val `prometheus-akka-http-exporter` = (project in file("prometheus-akka-http-exporter"))
    .settings(commonSettings)
    .settings(Seq(
      name := "prometheus-akka-http-exporter",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-http" % "10.1.1" % Provided,
        "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test
      ) ++ Seq(
        "com.typesafe.akka" %% "akka-actor",
        "com.typesafe.akka" %% "akka-stream").map(_ % "2.5.13" % Provided)
    )).dependsOn(`metric-prometheus`)

val metricsVersion = "4.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name                 := "metric-tools",
    organization         := "com.evolutiongaming",
    homepage             := Some(new URL("https://github.com/evolution-gaming/metric-tools")),
    startYear            := Some(2017),
    organizationName     := "Evolution",
    organizationHomepage := Some(url("https://www.evolution.com/")),
    scalaVersion         := crossScalaVersions.value.head,
    crossScalaVersions   := Seq("2.13.10", "2.12.17"),
    releaseCrossBuild    := true,
    licenses             := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),
    publishTo            := Some(Resolver.evolutionReleases),
    Compile / doc / scalacOptions += "-no-link-warnings",
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-core" % metricsVersion,
      "io.dropwizard.metrics" % "metrics-graphite" % metricsVersion,
      "com.typesafe" % "config" % "1.4.2",
      "com.github.pureconfig" %% "pureconfig" % "0.17.3",
      "com.evolutiongaming" %% "executor-tools" % "1.0.2"
    ),
  )

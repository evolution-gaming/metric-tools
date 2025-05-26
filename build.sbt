val metricsVersion = "4.1.1"

val pureconfigVersion = "0.17.8"

lazy val root = project
  .in(file("."))
  .settings(
    name := "metric-tools",
    organization := "com.evolutiongaming",
    homepage := Some(url("https://github.com/evolution-gaming/metric-tools")),
    startYear := Some(2017),
    organizationName := "Evolution",
    organizationHomepage := Some(url("https://www.evolution.com/")),
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq("2.13.16", "3.3.6"),
    licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT"))),
    publishTo := Some(Resolver.evolutionReleases),
    Compile / doc / scalacOptions += "-no-link-warnings",
    libraryDependencies ++= Seq(
      "io.dropwizard.metrics" % "metrics-core" % metricsVersion,
      "io.dropwizard.metrics" % "metrics-graphite" % metricsVersion,
      "com.typesafe" % "config" % "1.4.3",
      "com.github.pureconfig" %% "pureconfig-core" % pureconfigVersion,
      "com.evolutiongaming" %% "executor-tools" % "1.0.5",
    ),
    libraryDependencies ++= crossSettings(
      scalaVersion = scalaVersion.value,
      if2 = Seq("com.github.pureconfig" %% "pureconfig-generic" % pureconfigVersion),
      if3 = Seq("com.github.pureconfig" %% "pureconfig-generic-scala3" % pureconfigVersion),
    ),
    versionPolicyIntention := Compatibility.BinaryCompatible,
    addCommandAlias("check", "all versionPolicyCheck scalafmtCheckAll scalafmtSbtCheck"),
    addCommandAlias("fmt", "all scalafmtAll scalafmtSbt"),
    addCommandAlias("build", "+all compile test"),
  )

def crossSettings[T](scalaVersion: String, if3: T, if2: T): T =
  scalaVersion match {
    case version if version.startsWith("3") => if3
    case _ => if2
  }

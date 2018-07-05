import sbt._

object Version {
  val Config = "1.3.3"
  val ScalaTest = "3.0.5"
  val ExecutorTools = "1.0.0"
  val DropWizard = "3.2.5"
  val Prometheus = "0.4.0"
  val AkkaHttp = "10.1.3"
  val Akka = "2.5.13"
  val Shapeless = "2.3.3"
}

object Dependencies {
  val Config = "com.typesafe" % "config" % Version.Config
  val ScalaTest = "org.scalatest" %% "scalatest" % Version.ScalaTest % Test
  val ExecutorTools = "com.evolutiongaming" %% "executor-tools" % Version.ExecutorTools

  val DropWizard = Seq(
    "io.dropwizard.metrics" % "metrics-core" % Version.DropWizard,
    "io.dropwizard.metrics" % "metrics-graphite" % Version.DropWizard
  )

  val Prometheus = Seq(
    "io.prometheus" % "simpleclient_common" % Version.Prometheus,
    "io.prometheus" % "simpleclient_hotspot" % Version.Prometheus,
    "io.prometheus" % "simpleclient_logback" % Version.Prometheus
  )

  val AkkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % Version.AkkaHttp % Provided,
    "com.typesafe.akka" %% "akka-http-testkit" % Version.AkkaHttp % Test
  )

  val AkkaActor = "com.typesafe.akka" %% "akka-actor" % Version.Akka % Provided
  val AkkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.Akka % Provided

  val Shapeless = "com.chuusai" %% "shapeless" % Version.Shapeless
}
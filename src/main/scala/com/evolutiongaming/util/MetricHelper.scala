package com.evolutiongaming.util

import com.codahale.metrics._

import scala.compat.Platform
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

object MetricHelper {

  object GaugeF {
    def apply[T](f: => T): Gauge[T] = new Gauge[T] { def getValue: T = f }
  }

  implicit class RichTimer(val timer: Timer) extends AnyVal {
    def timeExceed[T](limit: FiniteDuration)(f: => T): T = {
      val start = Platform.currentTime
      try f finally {
        val stop = Platform.currentTime
        val duration = stop - start
        if (duration >= limit.toMillis) timer.update(duration, MILLISECONDS)
      }
    }

    def timeFuture[T](f: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
      val time = timer.time()
      try f andThen { case _ => time.stop() } catch {
        case NonFatal(e) => time.stop(); throw e
      }
    }

    def timeFunc[T](f: => T): T = {
      val time = timer.time()
      try f finally time.stop()
    }
  }

  implicit class HistogramOps(val histogram: Histogram) extends AnyVal {
    def timeFunc[T](f: => T): T = {
      val start = Platform.currentTime
      try f finally histogram.update(Platform.currentTime - start)
    }

    def timeFuture[T](f: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
      val start = Platform.currentTime
      f andThen { case _ => histogram.update(Platform.currentTime - start) }
    }
  }

  implicit class MetricRegistryOps(val self: MetricRegistry) extends AnyVal {
    def gauge[T](name: String, f: => T): Gauge[T] = {
      self remove name
      self.register(name, GaugeF(f))
    }
  }
}
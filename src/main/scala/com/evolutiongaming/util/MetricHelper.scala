package com.evolutiongaming.util

import com.codahale.metrics._
import com.evolutiongaming.concurrent.CurrentThreadExecutionContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.control.NonFatal

object MetricHelper {
  private implicit val ec = CurrentThreadExecutionContext

  object GaugeF {
    def apply[T](f: => T): Gauge[T] = new Gauge[T] {def getValue: T = f }
  }

  implicit class RichTimer(val timer: Timer) extends AnyVal {
    def timeExceed[T](limit: FiniteDuration)(f: => T): T = {
      val start = System.currentTimeMillis()
      try f finally {
        val stop = System.currentTimeMillis()
        val duration = stop - start
        if (duration >= limit.toMillis) timer.update(duration, MILLISECONDS)
      }
    }

    def timeFuture[T](f: => Future[T]): Future[T] = {
      val time = timer.time()
      try f andThen { case _ => time.stop() } catch {
        case NonFatal(e) => time.stop(); throw e
      }
    }

    def timeFunc[T](f: => T): T = {
      val time = timer.time()
      try f finally {
        time.stop()
        ()
      }
    }
  }

  implicit class HistogramOps(val histogram: Histogram) extends AnyVal {

    def timeFunc[T](f: => T): T = {
      val start = System.currentTimeMillis()
      try f finally histogram.timeTillNow(start)
    }

    def timeFuture[T](f: => Future[T]): Future[T] = {
      val start = System.currentTimeMillis()
      f andThen { case _ => histogram.timeTillNow(start) }
    }

    def timeTillNow(start: Long): Unit = {
      histogram.update(System.currentTimeMillis() - start)
    }
  }

  implicit class MetricRegistryOps(val self: MetricRegistry) extends AnyVal {
    def gauge[T](name: String, f: => T): Gauge[T] = {
      self remove name
      self.register(name, GaugeF(f))
    }
  }
}
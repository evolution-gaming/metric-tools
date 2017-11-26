package com.evolutiongaming.metrics

import scala.collection.concurrent.TrieMap

object MetricName {
  private val cache = TrieMap.empty[String, String]

  def apply(str: String): String = {
    def name = str
      .replace("com.evolutiongaming.", "")
      .split('$')
      .mkString("_")
      .replace(".", "_")

    cache.getOrElseUpdate(str, name)
  }

  def apply(clazz: Class[_]): String = apply(clazz.getName)
}
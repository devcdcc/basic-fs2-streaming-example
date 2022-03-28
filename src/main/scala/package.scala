package com.github

import cats.effect.IO
import fs2.Stream
import fs2.io.file.Path

package object devcdcc {

  case class HumiditySensorEvent(sensor: String, value: Option[Int])

  case class BasicAggregatedSensorData(sensor: String, count: Int, sum: Option[Int], min: Option[Int], max: Option[Int])

  trait WalkFolderEvents[F[K]] {
    def getFiles(folder: String): Stream[IO, Path]
    def retrieveFolderEvents(folder: Path): Stream[F, String]
  }

  trait InputFormatter[T] {
    def decode(line: String): T
  }

  trait DataReducer[F[K]] {
    /**
     *  this will be the most critical function, we will receive a simple structure that contains the sensor id and and its value.
     *  and also will return a map of elements that should contains the statistics, this map will be group by the sensor id.
     */
    def reduceMetrics(map: Map[String, BasicAggregatedSensorData],event: HumiditySensorEvent): Map[String,BasicAggregatedSensorData]

  }

  trait OutputFormatter[T] {
    def encode(data: T): String
  }

}

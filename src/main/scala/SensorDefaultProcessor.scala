package com.github.devcdcc

import fs2.{Stream, text}
import cats.effect.IO
import fs2.io.file.{Files, Path}


class SensorDefaultProcessor[F[K]] extends InputFormatter[HumiditySensorEvent] with DataReducer[F] with OutputFormatter[BasicAggregatedSensorData] {

  def decode(line: String): HumiditySensorEvent =
    Option(line).map(_.split(","))
      .map(row => HumiditySensorEvent(row(0), row(1).toIntOption))
      .get

  def reduceMetrics(acc: Map[String, BasicAggregatedSensorData], sensor: HumiditySensorEvent): Map[String,BasicAggregatedSensorData] = {
    val previousStats = acc.get(sensor.sensor).map(currentStats => {
      currentStats.copy(
        count = currentStats.count + 1,
        sum = currentStats.sum
          .flatMap(currentSum =>
            sensor.value.map(_ + currentSum)
              .orElse(Option(currentSum))
          )
          .orElse(sensor.value),
        min = currentStats.min
          .flatMap(currentMin =>
            sensor.value.map(sensorValue =>
              if (sensorValue < currentMin) sensorValue else currentMin
            ).orElse(Option(currentMin))
          )
          .orElse(sensor.value),
        max = currentStats.max
          .flatMap(currentMax =>
            sensor.value.map(sensorValue =>
              if (sensorValue > currentMax) sensorValue else currentMax
            )
              .orElse(Option(currentMax))
          )
          .orElse(sensor.value)

      )
    })
      .getOrElse(BasicAggregatedSensorData(sensor.sensor, count = 1, sum = sensor.value, min = sensor.value, max = sensor.value))
    acc + (sensor.sensor -> previousStats)
  }

  override def encode(data: BasicAggregatedSensorData): String =
    s"""${data.sensor},${data.sum.map(_.toDouble / data.count).getOrElse("NaN")},${data.min.getOrElse("NaN")},${data.max.getOrElse("NaN")}"""
}

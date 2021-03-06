package com.github.devcdcc

import org.scalatest.*
import org.scalatest.wordspec.*
import fs2.*
import cats.effect.{IO, Sync}
import matchers._

class DataReducerSpec extends AnyWordSpec with must.Matchers {
  val subject: DataReducer[Pure] = new SensorDefaultProcessor[Pure]
  "FoldData" can {
    "dataReducer" should {
      "return a valid list with a map of metrics" in {
        // given
        val input = Stream(
          HumiditySensorEvent("s1", Some(1)),
          HumiditySensorEvent("s2", Some(2)),
          HumiditySensorEvent("s3", None),
          HumiditySensorEvent("s4", Some(3)),
          HumiditySensorEvent("s3", None),
          HumiditySensorEvent("s4", None),
          HumiditySensorEvent("s1", Some(4)),
          HumiditySensorEvent("s2", Some(5)),
          HumiditySensorEvent("s1", Some(6)),
          HumiditySensorEvent("s3", None),
          HumiditySensorEvent("s1", Some(7))
        )
        val expected = List(Map(
          ("s1", BasicAggregatedSensorData("s1", 4, Option(18), Option(1), Option(7))),
          ("s2", BasicAggregatedSensorData("s2", 2, Option(7), Option(2), Option(5))),
          ("s3", BasicAggregatedSensorData("s3", 3, None, None, None)),
          ("s4", BasicAggregatedSensorData("s4", 2, Option(3), Option(3), Option(3)))
        ))
        // when
        val result: Stream[Pure, Map[String, BasicAggregatedSensorData]] = input.fold(Map.empty)(subject.reduceMetrics)
        // then
        result.toList mustBe expected

      }
    }
  }
}

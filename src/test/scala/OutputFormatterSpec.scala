package com.github.devcdcc
import org.scalatest.wordspec._
import org.scalatest.matchers._
class OutputFormatterSpec extends AnyWordSpec with must.Matchers {
  var subject: OutputFormatter[BasicAggregatedSensorData] = new SensorDefaultProcessor
    "OutputFormatter" can {
      "encode" should {
        "return a CSV line for a given BasicAggregatedSensorData" in {
          // given
          val metrics = BasicAggregatedSensorData("s1", 4, Option(22), Option(1), Option(6))
          // when
          val result = subject.encode(metrics)
          // then
          result mustBe "s1,5.5,1,6"
        }
      }
    }
}

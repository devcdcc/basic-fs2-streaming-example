package com.github.devcdcc
import org.scalatest.wordspec._
import org.scalatest.matchers._

class InputFormatterSpec extends AnyWordSpec with must.Matchers {
  var subject: InputFormatter[NonNumericHumidityEvent] = new SensorDefaultProcessor
  "OutputFormatter" can {
    "decode" should {
      "return HumidityEvent with None as Value" in {
        // given
        val line = "s1,NaN"
        val expected = NonNumericHumidityEvent("s1", None)
        // when
        val result = subject.decode(line)
        // then
        result mustBe expected
      }
      "return HumidityEvent with 1 as Value" in {
        // given
        val line = "s1,1"
        val expected = NonNumericHumidityEvent("s1", Option(1))
        // when
        val result = subject.decode(line)
        // then
        result mustBe expected
      }
    }
  }
}

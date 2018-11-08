package tracker.service

import tracker.Specification
import tracker.model.Delay

class DelayServiceTest extends Specification{

  "DelayService" should {
    "return delay amount by line name" in {

      val delayService = DelayService(Map("M4" -> Delay("M4", 3), "S5" -> Delay("S5", 6)))
      val result = delayService.findByLineName("S5")

      result shouldEqual 6
    }

    "return 0 when line not found" in {

      val delayService = DelayService(Map("M4" -> Delay("M4", 3), "S5" -> Delay("S5", 6)))
      val result = delayService.findByLineName("F4")

      result shouldEqual 0
    }
  }
}

package tracker.util

import tracker.Specification
import tracker.configuration.Tracker
import org.mockito.Mockito._
import tracker.model.Delay

class DataLoaderTest extends Specification {

  "DataLoader" should {
    "load Delays from file" in {
      val trackerConfig = mock[Tracker]
      when(trackerConfig.delays).thenReturn("data/delays.csv")

      val dataLoader = DataLoader(trackerConfig)
      val expectedResult = Map("M4" -> Delay("M4", 1), "200" -> Delay("200", 2), "S75" -> Delay("S75", 10))

      val result = dataLoader.loadDelays

      result.size shouldBe 3
      result shouldEqual expectedResult
    }
  }
}

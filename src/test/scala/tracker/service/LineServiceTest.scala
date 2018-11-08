package tracker.service

import tracker.Specification
import tracker.model.Line

class LineServiceTest extends Specification{

  "LineService" should {
    "return line by line id" in {

      val lineService = LineService(Map(1L -> Line(1L, "G3")))
      val expectedLine = Line(1L, "G3")

      val result = lineService.findById(1L)

      result should not be None
      result.get shouldEqual expectedLine
    }

    "return all lines" in {

      val lineService = LineService(Map(1L -> Line(1L, "G3"), 2L -> Line(2L, "M6")))
      val expectedResult = List(Line(1L, "G3"), Line(2L, "M6"))

      val result = lineService.findAll()

      result.size shouldEqual 2
      result shouldEqual expectedResult
    }
  }
}

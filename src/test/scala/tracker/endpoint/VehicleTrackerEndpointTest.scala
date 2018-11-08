package tracker.endpoint

import tracker.Specification
import akka.http.scaladsl.testkit.ScalatestRouteTest
import tracker.service.TrackerService
import org.mockito.Mockito._


class VehicleTrackerEndpointTest extends Specification with ScalatestRouteTest {

  "The VehicleTracerEndpoint at /tracker/info/line/{lineId}/delayed " when {
    "GET" should {
      "return true if the line is delayed" in {

        val trackerService = mock[TrackerService]
        when(trackerService.isLineDelayed(1)).thenReturn(true)
        val routes = new VehicleTrackerEndpoint(trackerService).routes

        Get("/tracker/info/line/1/delayed") ~> routes ~> check {
          entityAs[String] shouldBe "true"
        }
      }
    }
  }
}


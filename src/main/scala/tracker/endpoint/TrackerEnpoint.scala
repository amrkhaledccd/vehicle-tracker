package tracker.endpoint

import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCode}
import akka.http.scaladsl.server.Directives.complete
import tracker.model.Vehicle
import JsonSupport._
import spray.json.pimpAny


trait TrackerEnpoint {

    def completeWithStatus(statusCode: StatusCode, content: String) = complete(HttpResponse(status = statusCode, entity = content))

    def completeWithString(content: String) = complete(content)

    def completeWithVehicleResponse(vehicle: Vehicle) = {
      complete (
        VehicleResponse
        (
          vehicle.vehicleId,
          vehicle.line.id,
          vehicle.line.name
        ).toJson.prettyPrint
      )
  }
}

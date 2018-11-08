package tracker.endpoint

import spray.json.DefaultJsonProtocol


object JsonSupport extends DefaultJsonProtocol {
  implicit val vehicleResponse = jsonFormat3(VehicleResponse)
}

case class VehicleResponse (vehicleId: Long,  lineId: Long, lineName: String)

package tracker.endpoint


import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import tracker.model.Coordinate
import tracker.service.TrackerService
import tracker.util.TimeUtil

import scala.util.{Failure, Success, Try}


class VehicleTrackerEndpoint(trackerService: TrackerService)
                            (implicit actorSystem: ActorSystem, actorMaterializer: ActorMaterializer)
    extends TrackerEnpoint with LazyLogging{

  val routes = pathPrefix("tracker"/"info") {

    path("vehicle") {
      parameters("time", "x".as[Int], "y".as[Int]) { (time: String, x, y) =>
        findVehicleByTimeAndCoordinate(time, x, y)
      }
    } ~
      path("vehicle") {
        parameters("stopId".as[Int]) { stopId =>
          findNextVehicle(stopId)
        }
      } ~
      path("line"/LongNumber/"delayed") { lineId =>
        isLineDelayed(lineId)
      }
  }

  private def findVehicleByTimeAndCoordinate(time: String, x: Int, y: Int): Route = get {

    Try(TimeUtil.parseTime(time)) match {
      case Success(lTime) => {

        trackerService.findVehicleByTimeAndCoordinate(lTime, Coordinate(x, y)) match {
          case Some(vehicle) => completeWithVehicleResponse(vehicle)
          case None => completeWithStatus(StatusCodes.NoContent, "No Vehicle found")
        }
      }
      case Failure(exception) => {
        logger.error("Error parsing time ", exception)
        completeWithStatus(StatusCodes.BadRequest, s"Couldn't parse time $time")
      }
    }
  }

  private def findNextVehicle(stopId: Int): Route = {

    trackerService.findNextVehicle(stopId) match  {
      case Some(vehicle) => completeWithVehicleResponse(vehicle)
      case None => completeWithStatus(StatusCodes.NoContent, "No Vehicle found")
    }
  }

  private def isLineDelayed(lineId: Long): Route =
    completeWithString(trackerService.isLineDelayed(lineId).toString)
}

package tracker.service

import java.time.LocalTime

import com.typesafe.scalalogging.LazyLogging
import tracker.model.{Coordinate, StopTime, Vehicle}

import scala.util.Try

/*
  Main functionalities that are exposed through the endpoint
 */
trait TrackerOperations {

  def findVehicleByTimeAndCoordinate(time: LocalTime, coordinate: Coordinate): Try[Option[Vehicle]]

  def findNextVehicle(stopId: Long): Try[Option[Vehicle]]

  def isLineDelayed(lineId: Long): Try[Boolean]
}

/*
  for fast access it holds a map [LineId, List[Vehicle]]
  List of vehicles that operates on that line
  I assume only one vehicle that operates on a line but it can be extended
  to many vehicles, after all it is a list
 */
class TrackerService(lineVehicles: Map[Long, List[Vehicle]],
                     delayService: DelayService,
                     lineService: LineService,
                     stopService: StopService,
                     stopTimeService: StopTimeService)
  extends TrackerOperations
    with LazyLogging {

  /*
    Finds vehicle by time and coordinates
    Steps:
     1. find stop Id by coordinates
     2. find stopTime that its time + delay equal to specified time
     3. find the vehicle for this stop time
   */
  override def findVehicleByTimeAndCoordinate(time: LocalTime,
                                              coordinate: Coordinate): Try[Option[Vehicle]] =
  {

    val stopId = stopService.findByByCoordinate(coordinate) match {
      case Some(sId) => sId.id
      case None => throw new RuntimeException(s"Stop not found for coordinate $coordinate")
    }

    Try(
        for {
          stopTime <- findStopTime(stopId, time, (time1, time2) => time1.equals(time2));
          vehicle <- findVehicle(stopTime)
        } yield vehicle
      )
  }

  /*
    Finds first vehicle will arrive next to the specified stop
   */
  override def findNextVehicle(stopId: Long): Try[Option[Vehicle]] = {
    Try (
      for {
        stopTime <- findStopTime(stopId, LocalTime.now(), (time1, time2) => time1.compareTo(time2) >= 0);
        vehicle <- findVehicle(stopTime)
      } yield vehicle
    )
  }

  /*
    Checks if the line is delayed
    For this requirement as far as I understood, if there is an entry in delays.csv for the line
    that is greater than 0 then the line is delayed.

    for the given data sample all lines are delayed
   */
  override def isLineDelayed(lineId: Long): Try[Boolean] = Try (
    delayService.findByLineName(findLineName(lineId)) > 0
  )

  /*
     Finds stopTime that its time + delay equal or greater and equal (based on compare function)
     to specified time
   */
  private def findStopTime(stopId: Long, time: LocalTime, compare: (LocalTime, LocalTime) => Boolean) = {
    stopTimeService.findByStopId(stopId) match {

      case Some(sTimes) => sTimes.find { sTime =>
        val delay = delayService.findByLineName(findLineName(sTime.LineId))
        compare(sTime.time.plusMinutes(delay), time)
      }

      case None => throw new RuntimeException(s"No times found for stop Id $stopId")
    }
  }

  /*
    Finds the vehicle for this stop time
   */
  private def findVehicle(stopTime: StopTime) = {
    lineVehicles.get(stopTime.LineId) match {
      case Some(vehicles) => vehicles.find(_.stopTimes.contains(stopTime))
      case None => throw new RuntimeException(s"No vehicles found for line Id ${stopTime.LineId}")
    }
  }

  /*
    This is helper method to handle if line is not exist
   */
  private def findLineName(lineId: Long) = {

    lineService.findById(lineId) match {
      case Some(line) => line.name
      case None => throw new RuntimeException(s"No line found for lineId $lineId")
    }
  }
}

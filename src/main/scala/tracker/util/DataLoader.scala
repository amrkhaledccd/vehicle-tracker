package tracker.util

import com.typesafe.scalalogging.LazyLogging
import tracker.configuration.Tracker
import tracker.mapper.{GenericMapper, TrackerMapper}
import tracker.model._
import tracker.service.{LineService, StopTimeService}

import scala.collection.mutable
import scala.util.{Failure, Success}

object DataLoader {

  def apply(trackerConfig: Tracker): DataLoader = new DataLoader(trackerConfig)
}

class DataLoader(trackerConfig: Tracker) extends LazyLogging{

  def loadDelays: Map[String, Delay] = {

    val mutableMap: mutable.Map[String, Delay] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.delays)
      .drop(1)
      .foreach ( delayLine =>
        GenericMapper.map[Delay](delayLine, TrackerMapper.mapDelay) match {
          case Success(delay) => mutableMap.put(delay.lineName, delay)
          case Failure(exception) => logger.error(s"Error mapping delay line $delayLine ", exception)
        })

    mutableMap.toMap
  }

  def loadLines: Map[Long, Line] = {

    val mutableMap: mutable.Map[Long, Line] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.lines)
      .drop(1)
      .foreach(lineLine =>
        GenericMapper.map[Line](lineLine, TrackerMapper.mapLine) match {
          case Success(line) => mutableMap.put(line.id, line)
          case Failure(exception) => logger.error(s"Error mapping line $lineLine ", exception)
        })

    mutableMap.toMap
  }

  def loadStops: Map[Coordinate, Stop] = {

    val mutableMap: mutable.Map[Coordinate, Stop] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.stops)
      .drop(1)
      .foreach (stopLine =>
        GenericMapper.map[Stop](stopLine, TrackerMapper.mapStop) match {
          case Success(stop) => mutableMap.put(stop.coordinate, stop)
          case Failure(exception) => logger.error(s"Error mapping stop line $stopLine ", exception)

        })

    mutableMap.toMap
  }

  def loadStopTime: Map[Long, List[StopTime]] = {

    val mutableMap: mutable.Map[Long, List[StopTime]] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.times)
      .drop(1)
      .foreach (stopTimeLine =>
        GenericMapper.map[StopTime](stopTimeLine, TrackerMapper.mapTime) match {

          case Success(stopTime) => mutableMap.get(stopTime.stopId) match {
                      case Some(times) => mutableMap.put(stopTime.stopId, times :+ stopTime)
                      case None => mutableMap.put(stopTime.stopId, List(stopTime))
                    }
          case Failure(exception) => logger.error(s"Error mapping stopTime line $stopTimeLine ", exception)

        })

    mutableMap.toMap
  }

  /*
    For this I will initialize one vehicle for each line.
    This also could be provided through a CSV file like above
   */
  def loadVehicles(lineService: LineService, stopTimeService: StopTimeService): Map[Long, List[Vehicle]] = {

    val mutableMap: mutable.Map[Long, List[Vehicle]] = mutable.Map()
    // Just an incremental number for vehicle Id
    var vehicleId = 1

    lineService.findAll().map { line =>

      val stopTimes = stopTimeService.findByLineId(line.id)
      val vehicle = Vehicle(vehicleId, stopTimes, line)

      vehicleId += 1
      mutableMap.put(line.id, List(vehicle))
    }

    mutableMap.toMap
  }
}

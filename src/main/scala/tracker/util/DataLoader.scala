package tracker.util

import tracker.configuration.Tracker
import tracker.mapper.{GenericMapper, TrackerMapper}
import tracker.model._
import tracker.service.{LineService, StopTimeService}

import scala.collection.mutable

object DataLoader {

  def apply(trackerConfig: Tracker): DataLoader = new DataLoader(trackerConfig)
}

class DataLoader(trackerConfig: Tracker) {

  def loadDelays: Map[String, Delay] = {

    val mutableMap: mutable.Map[String, Delay] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.delays)
      .drop(1)
      .map(GenericMapper.map[Delay](_, TrackerMapper.mapDelay))
      .foreach(delay => mutableMap.put(delay.lineName, delay))

    mutableMap.toMap
  }

  def loadLines: Map[Long, Line] = {

    val mutableMap: mutable.Map[Long, Line] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.lines)
      .drop(1)
      .map(GenericMapper.map[Line](_, TrackerMapper.mapLine))
      .foreach(line => mutableMap.put(line.id, line))

    mutableMap.toMap
  }

  def loadStops: Map[Coordinate, Stop] = {

    val mutableMap: mutable.Map[Coordinate, Stop] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.stops)
      .drop(1)
      .map(GenericMapper.map[Stop](_, TrackerMapper.mapStop))
      .foreach(stop => mutableMap.put(stop.coordinate, stop))

    mutableMap.toMap
  }

  def loadStopTime: Map[Long, List[StopTime]] = {

    val mutableMap: mutable.Map[Long, List[StopTime]] = mutable.Map()

    TrackerFileReader
      .readLinesFromResource(trackerConfig.times)
      .drop(1)
      .map(GenericMapper.map[StopTime](_, TrackerMapper.mapTime))
      .foreach(time => mutableMap.get(time.stopId) match {
          case Some(times) => mutableMap.put(time.stopId, times :+ time)
          case None => mutableMap.put(time.stopId, List(time))
        }
      )

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

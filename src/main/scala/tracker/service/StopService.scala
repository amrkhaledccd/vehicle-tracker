package tracker.service

import com.typesafe.scalalogging.LazyLogging
import tracker.model.{Coordinate, Stop}


trait StopOperations {
  def findByStopId(stopId: Long): Option[Stop]
  def findByCoordinate(coordinate: Coordinate): Option[Stop]
}

object StopService {
  def apply(stopMap: Map[Coordinate, Stop]): StopService = new StopService(stopMap)
}

/*
  for fast access it holds a map [Coordinate, Stop]
 */
class StopService(stopMap: Map[Coordinate, Stop]) extends StopOperations
  with LazyLogging {

  override def findByStopId(stopId: Long): Option[Stop] =
    stopMap.values.find(_.id == stopId)

  override def findByCoordinate(coordinate: Coordinate): Option[Stop] =
    stopMap.get(coordinate)
}

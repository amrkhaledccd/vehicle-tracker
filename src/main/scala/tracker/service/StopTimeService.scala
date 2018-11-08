package tracker.service


import tracker.model.StopTime


trait StopTimeOperations {
  def findByStopId(stopId: Long): Option[List[StopTime]]
  def findByLineId(lineId: Long): List[StopTime]
}

object StopTimeService {
  def apply(stopTimeMap: Map[Long, List[StopTime]]): StopTimeService = new StopTimeService(stopTimeMap)
}

/*
  for fast access it holds a map [stopId, List[stopTime]]
 */
class StopTimeService(stopTimeMap: Map[Long, List[StopTime]]) extends StopTimeOperations {

  override def findByStopId(stopId: Long): Option[List[StopTime]] =
    stopTimeMap.get(stopId)

  override def findByLineId(lineId: Long): List[StopTime] =
    stopTimeMap.flatMap(_._2).filter(_.LineId == lineId).toList

}

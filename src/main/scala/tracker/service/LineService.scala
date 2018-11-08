package tracker.service

import com.typesafe.scalalogging.LazyLogging
import tracker.model.Line

trait LineOperations {
  def findById(LineId: Long): Option[Line]
  def findAll(): List[Line]
}

object LineService {
  def apply(lineMap: Map[Long, Line]): LineService = new LineService(lineMap)
}

/*
  for fast access it holds a map [LineId, Line]
 */
class LineService(lineMap: Map[Long, Line]) extends LineOperations
    with LazyLogging {

  override def findById(lineId: Long): Option[Line] = lineMap.get(lineId)

  override def findAll(): List[Line] = lineMap.values.toList
}

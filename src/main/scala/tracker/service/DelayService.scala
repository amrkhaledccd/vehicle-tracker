package tracker.service

import com.typesafe.scalalogging.LazyLogging
import tracker.model.Delay

/*
  I assumed that if delay is not provided, its value will equal to zero
 */

trait DelayOperations {
  def findByLineName(lineName: String): Int
}


object DelayService {
  def apply(lineDelays: Map[String, Delay]): DelayService = new DelayService(lineDelays)
}

/*
  for fast access it holds a map [LineName, Delay]
 */
class DelayService(lineDelays: Map[String, Delay]) extends DelayOperations
    with LazyLogging {

  override def findByLineName(lineName: String): Int =
    lineDelays.get(lineName) match {
      case Some(delay) => delay.amount
      case None => {
        logger.warn(s"Delay not found for line id $lineName, will return 0")
        0
      }
    }
}

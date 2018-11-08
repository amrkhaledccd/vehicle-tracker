package tracker.mapper

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import tracker.model._


object GenericMapper {

  val delimiter = ","

  def map[T](line: String, mapFun: Array[String] => T): T = mapFun(line.split(delimiter))
}

/*
  For simplicity I assumed that the data is syntactically correct, so no validation.
  If this is not the case, I would create a data validator to validate each line before mapping
 */
object TrackerMapper {

  val timeFormatter = DateTimeFormatter.ISO_TIME

  def mapDelay(arr: Array[String]) = Delay(arr(0), arr(1).toInt)

  def mapLine(arr: Array[String]) = Line(arr(0).toInt, arr(1))

  def mapStop(arr: Array[String]) = Stop(arr(0).toInt, Coordinate(arr(1).toInt, arr(2).toInt))

  def mapTime(arr: Array[String]) = StopTime(arr(0).toInt, arr(1).toInt,  LocalTime.parse(arr(2), timeFormatter))
}

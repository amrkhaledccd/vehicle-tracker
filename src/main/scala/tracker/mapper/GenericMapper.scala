package tracker.mapper

import java.time.format.DateTimeFormatter

import tracker.model._
import tracker.util.TimeUtil

import scala.util.Try


object GenericMapper {

  val delimiter = ","

  def map[T](line: String, mapFun: Array[String] => Try[T]): Try[T] = mapFun(line.split(delimiter))
}

/*
  For simplicity I assumed that the data is syntactically correct, so no validation, However,
  If line is failed to be mapped, I will log the error and skip the line.
  If this is not the case, I would create a data validator to validate each line before mapping
 */
object TrackerMapper {

  val timeFormatter = DateTimeFormatter.ISO_TIME

  def mapDelay(arr: Array[String]) = Try(Delay(arr(0), arr(1).toInt))

  def mapLine(arr: Array[String]) = Try(Line(arr(0).toInt, arr(1)))

  def mapStop(arr: Array[String]) = Try(Stop(arr(0).toInt, Coordinate(arr(1).toInt, arr(2).toInt)))

  def mapTime(arr: Array[String]) = Try(StopTime(arr(0).toInt, arr(1).toInt,  TimeUtil.parseTime(arr(2))))
}

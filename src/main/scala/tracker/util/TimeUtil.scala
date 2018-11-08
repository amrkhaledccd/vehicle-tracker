package tracker.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter


object TimeUtil {

  val timeFormatter = DateTimeFormatter.ISO_TIME

  def parseTime(time: String): LocalTime =
    LocalTime.parse(time, timeFormatter)


  def now = LocalTime.now()
}

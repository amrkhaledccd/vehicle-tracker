package tracker.model

import java.time.LocalTime


case class Line (id: Long, name: String)

case class Coordinate (x: Long, y: Long)

case class Stop (id: Long, coordinate: Coordinate)

case class Delay (lineName: String, amount: Int)

case class StopTime (LineId: Long,  stopId: Long, time: LocalTime)

case class Vehicle (vehicleId: Long, stopTimes: List[StopTime], line: Line)
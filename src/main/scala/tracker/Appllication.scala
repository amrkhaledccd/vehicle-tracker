package tracker

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import tracker.configuration.ConfigurationService
import tracker.service._
import tracker.util.DataLoader


object Appllication extends App with LazyLogging {

  // Services initialization
  val configuration = new ConfigurationService(ConfigFactory.load())
  val dataLoader = DataLoader(configuration.tracker)
  val delayService = DelayService(dataLoader.loadDelays)
  val lineService = LineService(dataLoader.loadLines)
  val stopService = StopService(dataLoader.loadStops)
  val stopTimeService = StopTimeService(dataLoader.loadStopTime)

  val trackerService: TrackerService = new TrackerService(
    dataLoader.loadVehicles(lineService, stopTimeService),
    delayService, lineService, stopService, stopTimeService
  )

  //Initialize the endpoint


  logger.info("done")
}

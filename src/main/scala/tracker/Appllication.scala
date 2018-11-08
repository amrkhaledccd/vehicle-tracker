package tracker

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import tracker.configuration.ConfigurationService
import tracker.endpoint.VehicleTrackerEndpoint
import tracker.service._
import tracker.util.DataLoader

import scala.util.{Failure, Success}


object Application extends App with LazyLogging {

  //Create an actorSystem
  implicit val actorSystem = ActorSystem("vehicle-tracker-system")
  implicit var ec = actorSystem.dispatcher
  implicit lazy val materializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))


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
  val vehicleTrackerEndpoint = new VehicleTrackerEndpoint(trackerService)

  val init = for {
    server <- Http().bindAndHandle(vehicleTrackerEndpoint.routes,
      configuration.server.interface, configuration.server.port)
  } yield server

  init.andThen {
    case Success(binding) => logger.info("successfully started Vehicle-Tracker")
    case Failure(exception) => {
      logger.error("Could not start Vehicle-tracker", exception)
      actorSystem.terminate()
    }
  }
}

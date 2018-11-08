package tracker.configuration

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

/**
  * Centralized configuration class, here one can find all configuration needed in this service.
  */
class ConfigurationService(configuration: Config) {
  lazy val server = configuration.as[Server]("server")
  lazy val tracker = configuration.as[Tracker]("tracker")
}

case class Server (port: Int, interface: String)
case class Tracker (delays: String, lines: String, stops: String, times: String)
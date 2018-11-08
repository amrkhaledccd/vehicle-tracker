package tracker.util

import com.typesafe.scalalogging.LazyLogging

import scala.io.Source
import scala.util.{Failure, Success, Try}

object TrackerFileReader extends LazyLogging {

  /*
    I let it fail in case it is unable to read the files
   */
  def readLinesFromResource(fileName: String): Iterator[String] =
    Try(Source.fromResource(fileName).getLines()) match {
      case Success(lines) => lines
      case Failure(exception) => {
        logger.error(s"Error while reading file $fileName - message $exception")
        throw new RuntimeException(s"Error loading file $fileName")
      }
    }

}



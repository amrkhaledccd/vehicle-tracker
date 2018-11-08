package tracker

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

trait Specification extends WordSpec with Matchers with MockitoSugar with BeforeAndAfter {

//  implicit val system = ActorSystem("actor-system")
//  implicit val materializer = ActorMaterializer()
}


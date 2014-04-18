package sample.remote.interpreter

import akka.actor.Props
import akka.actor.Actor

class InterpreterActor extends Actor {
  def receive = {
    case Code(toRun) =>
      println(s"interpreting $toRun")
      sender ! CodeResult(s"The result was ${toRun.reverse}")
  }
}


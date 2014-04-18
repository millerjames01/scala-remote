package sample.remote.interpreter

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class CreationActor extends Actor {

  def receive = {
    case code: Code =>
      val interpreter = context.actorOf(Props[InterpreterActor])
      interpreter ! code
    case CodeResult(result) => 
      println(result)
  }
}

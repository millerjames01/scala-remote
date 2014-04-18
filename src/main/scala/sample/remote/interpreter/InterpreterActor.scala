package sample.remote.interpreter

import akka.actor.Props
import akka.actor.Actor

class InterpreterActor extends Actor {
  def receive = {
    case Code(toRun) =>
      val rcvMessage = "Received \"" + toRun + "\"" 
      val resMessage = "Result \""+ Repl(toRun) + "\""
      println(rcvMessage)
      sender ! CodeResult(s"$rcvMessage | $resMessage")
  }
}


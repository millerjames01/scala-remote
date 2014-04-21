package sample.remote.interpreter

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import scala.sys.process._
import java.io.File

class CreationActor extends Actor {
  import CreationActor._
  
  var process: Process = null
  
  def receive = {
    case StartSystem =>
      val theCommand = Process("sbt run") #> (new File("result.txt"))
      process = theCommand.run
    case EndSystem =>
      if(process == null) println("System not running")
      else process.destroy
    case code: Code =>
      val interpreter = context.actorOf(Props[InterpreterActor])
      interpreter ! code
    case CodeResult(result) => 
      println(result)
  }
}

object CreationActor {
  case object StartSystem
  case object EndSystem
}
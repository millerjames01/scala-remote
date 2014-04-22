package sample.remote.interpreter

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import scala.sys.process._
import java.io.File

class CreationActor extends Actor {
  import CreationActor._
  
  var count: Int = 0
  var process: Process = null
  
  def receive = {
    case StartSystem =>
      val theCommand = Process("sbt run") #> (new File(s"result$count.txt"))
      process = theCommand.run
      count = count + 1
    case EndSystem =>
      if(process == null) println("System not running")
      else {
        process.destroy
        println("process destroyed")
        // check to see if port is busy
        // if so, really kill the process
        // self ! StartSystem
      }
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
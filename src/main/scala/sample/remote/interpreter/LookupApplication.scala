package sample.remote.interpreter

import scala.concurrent.duration._
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

object LookupApplication {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "Calculator")
      startRemoteCalculatorSystem()
    if (args.isEmpty || args.head == "Lookup")
      startRemoteLookupSystem()
  }

  def startRemoteCalculatorSystem(): Unit = {
    val system = ActorSystem("InterpreterSystem",
      ConfigFactory.load("interpreter"))
    system.actorOf(Props[InterpreterActor], "interpreter")

    println("Started InterpreterSystem - waiting for messages")
  }

  def startRemoteLookupSystem(): Unit = {
    val system =
      ActorSystem("LookupSystem", ConfigFactory.load("remotelookup"))
    val remotePath =
      "akka.tcp://CalculatorSystem@127.0.0.1:2552/user/calculator"
    val actor = system.actorOf(Props(classOf[LookupActor], remotePath), "lookupActor")

    println("Started LookupSystem") 
    import system.dispatcher
    system.scheduler.schedule(1.second, 1.second) {
      val letters = for (i <- 'a' to 'z') yield i
      val chars = for(i <- 1 to 5) yield letters(Random.nextInt(26))
      actor ! Code(chars.mkString)
    }
  }
}

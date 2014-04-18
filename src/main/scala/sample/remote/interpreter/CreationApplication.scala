package sample.remote.interpreter

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import scala.util.Random
import akka.actor.ActorSystem
import akka.actor.Props

object CreationApplication {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "InterpreterWorkerSystem")
      startRemoteWorkerSystem()
    if (args.isEmpty || args.head == "Creation")
      startRemoteCreationSystem()
  }

  def startRemoteWorkerSystem(): Unit = {
    ActorSystem("InterpreterWorkerSystem", ConfigFactory.load("interpreter"))
    println("Started InterpreterWorkerSystem")
  }

  def startRemoteCreationSystem(): Unit = {
    val system =
      ActorSystem("CreationSystem", ConfigFactory.load("remotecreation"))
    val actor = system.actorOf(Props[CreationActor],
      name = "creationActor")

    println("Started CreationSystem")
    import system.dispatcher
    system.scheduler.schedule(1.second, 1.second) {
      val letters = for (i <- 'a' to 'z') yield i
      val chars = for(i <- 1 to 5) yield letters(Random.nextInt(26))
      actor ! Code(chars.mkString)
    }

  }
}

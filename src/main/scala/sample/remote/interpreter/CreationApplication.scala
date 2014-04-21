package sample.remote.interpreter

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import scala.util.Random
import akka.actor.ActorSystem
import akka.actor.Props

object CreationApplication {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startRemoteWorkerSystem()
    if (args.head == "Creation")
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
    import CreationActor._
    val alphabet = 'a' to 'z'
    def oneToTen = Random.nextInt(10) + 1
    system.scheduler.schedule(20.second, 5.second) {
      val firstPick = Random.nextInt(26)
      val secondPick = (firstPick + 1 + Random.nextInt(25)) % 26
      system.scheduler.scheduleOnce(1.second)(actor ! Code(s"val ${alphabet(firstPick)} = ${oneToTen}"))
      system.scheduler.scheduleOnce(2.second)(actor ! Code(s"val ${alphabet(secondPick)} = ${oneToTen}"))
      system.scheduler.scheduleOnce(3.second)(actor ! Code(s"${alphabet(firstPick)} + ${alphabet(secondPick)}"))
    }
    actor ! StartSystem
    system.scheduler.scheduleOnce(10.seconds){
      actor ! EndSystem
    }
  }
}

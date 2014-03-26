name := "scala-remote"

version := "0.1"

scalaVersion := "2.10.2"

resolvers += "Akka Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-remote" % "2.2-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % "2.2-SNAPSHOT"
)


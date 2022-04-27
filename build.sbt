ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "KeepId-UserComms"
  )

val AkkaVersion = "2.6.19"
val AkkaHttpVersion = "10.2.9"
val KafkaVersion = "3.1.0"

// Akka
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion

// Json parser
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"

// Kafka Client
libraryDependencies += "org.apache.kafka" % "kafka-clients" % KafkaVersion

// Cassandra
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.3.0"
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "3.0.4"
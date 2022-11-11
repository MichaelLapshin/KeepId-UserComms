ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "KeepId-UserComms"
  )

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"
val KafkaVersion = "3.3.1"
val MySQLVersion = "8.0.30"

// Akka
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

// Logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.4"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

// Json parser
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"

// Kafka Client
libraryDependencies += "org.apache.kafka" % "kafka-clients" % KafkaVersion

// MySQL java
libraryDependencies += "mysql" % "mysql-connector-java" % MySQLVersion

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"

val commonLibraries = Seq(
  // Akka
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,

  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,

  // Logging
  "ch.qos.logback" % "logback-classic" % "1.4.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",

  // Json parser
  "io.spray" %% "spray-json" % "1.3.6",

  // Kafka Client
  "org.apache.kafka" % "kafka-clients" % "3.3.2",

  // MySQL java
//  "mysql" % "mysql-connector-java" % "8.0.30",

  // Azure SQL Server
  "com.microsoft.sqlserver" %% "mssql-jdbc" % ""
)

ThisBuild / assemblyMergeStrategy := {
  case "module-info.class" => MergeStrategy.discard
  case PathList("google", "protobuf", "struct.proto") => MergeStrategy.first
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val root = (project in file("."))
  .settings(
    name := "KeepId-UserComms",
  )
  .aggregate(common,
    company_request_manager,
    encrypted_data_sender,
    user_id_manager,
    user_request_fetcher,
    user_request_manager,
    user_response,
    user_update
  )

lazy val common = (project in file("common"))
  .settings(libraryDependencies ++= commonLibraries)

lazy val company_request_manager = (project in file("company_request_manager"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val encrypted_data_sender = (project in file("encrypted_data_sender"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val user_id_manager = (project in file("user_id_manager"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val user_request_fetcher = (project in file("user_request_fetcher"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val user_request_manager = (project in file("user_request_manager"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val user_response = (project in file("user_response"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

lazy val user_update = (project in file("user_update"))
  .settings(libraryDependencies ++= commonLibraries)
  .dependsOn(common)

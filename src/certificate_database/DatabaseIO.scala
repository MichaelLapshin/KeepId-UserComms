package certificate_database

import akka.actor.typed.ActorSystem
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraSession, CassandraSessionRegistry}
import akka.stream.scaladsl.Sink

import com.datastax.driver.core._
import scala.concurrent.Future

/**
 * @file: DatabseIO.scala
 * @description: Abstracts away the reading and writing producedures to the database.
 */

object DatabaseIO {
  var initialized: Boolean = false

  val system: ActorSystem = ActorSystem("System")
  val sessionSettings = CassandraSessionSettings()
  implicit val cassandraSession: CassandraSession =
    CassandraSessionRegistry.get(system).sessionFor(sessionSettings)

  val version: Future[String] =
    cassandraSession
      .select("SELECT release_version FROM system.local;")
      .map(_.getString("release_version"))
      .runWith(Sink.head)

}

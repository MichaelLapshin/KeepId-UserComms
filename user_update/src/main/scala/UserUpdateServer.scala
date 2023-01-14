package keepid.services.user_update

/**
 * @file: UserUpdateServer.scala
 * @description: The object in charge of starting and stopping the User Update Server.
 * @author: KeepId Inc.
 * @date: March 30, 2022
 */

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}

import scala.util.{Failure, Success}
import com.typesafe.scalalogging.Logger
import keepid.common.client_database.ClientDatabase
import keepid.common.message_broker.Connection

import java.security.SecureRandom
import javax.net.ssl.SSLContext
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.io.StdIn
import scala.sys.process.Process

object UserUpdateServer {
  // Server variables
  private val log = Logger(getClass.getName)
  val (host: String, port: Int) = ("localhost", 8001)
  val ShutdownTime: FiniteDuration = 30.seconds

  def main(args: Array[String]) = {
    log.info("Starting the User Update Server...")

    // Loads the server certificate
    log.info("Checking for the certificate.")
    if ( false /* TODO, complete this certificate check */ ) {
      log.error("Server certificate could not be found.")
      sys.exit(1)
    }

    implicit val server = ActorSystem(Behaviors.empty, "user-update")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = server.executionContext

    // Starts the Http server
    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(null, null, new SecureRandom)
    val https: HttpsConnectionContext = ConnectionContext.httpsServer(sslContext)

    val serverInstance = Http().newServerAt(host, port)
    serverInstance
      .enableHttps(https)
      .bind(UserUpdateRoute.UpdateRoute)
      .map(_.addToCoordinatedShutdown(hardTerminationDeadline = ShutdownTime))
      .foreach { _ =>
        log.info(s"HTTP service listening on: ${server.address}")
        server.whenTerminated.onComplete {
          case Success(_) => log.info("Shutdown of HTTP endpoint completed.")
          case Failure(_) => log.info("Shutdown of HTTP endpoint failed.")
        }
      }

    log.info("The server has been started.")
    Connection.waitUntilUserEndsServer()

    ClientDatabase.closeConnection()
    log.info("The server has been stopped.")
  }

}

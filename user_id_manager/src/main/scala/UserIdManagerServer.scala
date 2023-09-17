package keepid.services.user_id_manager

/**
 * @file: UserIdManagerServer.scala
 * @description: The object in charge of starting and stopping the User ID Manager Server.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.Done
import akka.actor.CoordinatedShutdown

import scala.util.{Failure, Success}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import akka.stream.TLSClientAuth
import com.typesafe.scalalogging.Logger

import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.io.StdIn
import scala.sys.process.Process
import keepid.common.message_broker.Connection
import keepid.common.client_database.ClientDatabase

import java.io.FileInputStream

object UserIdManagerServer {
  private val log = Logger(getClass.getName)
  val (host: String, port: Int) = ("localhost", 8002)
  val ShutdownTime: FiniteDuration = 1.minute

  def main(args: Array[String]) = {
    log.info("Starting the User ID Manager Server...")

//    // CHAT GPT ////////////
//    // Load the keystore
//    val ks = KeyStore.getInstance("PKCS12")
//    val keystoreFile = new FileInputStream("path/to/keystore.p12")
//    ks.load(keystoreFile, "keystore-password".toCharArray)
//
//    // Create the key manager factory
//    val kmf = KeyManagerFactory.getInstance("SunX509")
//    kmf.init(ks, "key-password".toCharArray)
//
//    // Create the trust manager factory
//    val tmf = TrustManagerFactory.getInstance("SunX509")
//    tmf.init(ks)
//
//    // Create the SSL context
//    val sslContext = SSLContext.getInstance("TLS")
//    sslContext.init(kmf.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
//
//    // Create the HTTPS connection context
//    val https: HttpsConnectionContext = ConnectionContext.httpsServer(sslContext)
//
//    ////////////////////////

    // Loads the server certificate
    log.info("Checking for the certificate.")
    if (false /* TODO, complete this certificate check */ ) {
      log.error("Server certificate could not be found.") // TODO, enable SSL on the Azure database. "Server parameters"
      sys.exit(1)
    }

    ClientDatabase.openConnection()

    implicit val server = ActorSystem(Behaviors.empty, "user-id-manager")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = server.executionContext

    // Starts the Http server
//    val sslContext: SSLContext = SSLContext.getInstance("TLS")
//    sslContext.init(null, null, new SecureRandom)
//    val https: HttpsConnectionContext = ConnectionContext.httpsServer(sslContext)

    val serverInstance = Http().newServerAt(host, port)
    serverInstance
//      .enableHttps(https)
      .bind(UserIdManagerRoute.IdManagerRoute)
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

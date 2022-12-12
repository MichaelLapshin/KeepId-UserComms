package services.user_update

/**
 * @file: UserUpdateServer.scala
 * @description: The object in charge of starting and stopping the User Update Server.
 * @author: KeepId Inc.
 * @date: March 30, 2022
 */

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import common.client_database.ClientDatabase
import common.message_broker.Producer
import com.typesafe.scalalogging.Logger

import java.security.SecureRandom
import javax.net.ssl.SSLContext
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.io.StdIn

object UserUpdateServer {
  // Server variables
  private val log = Logger(getClass.getName)
  var running = false;
  val (host: String, port: Int) = ("localhost", 8001)
  val ShutdownTime: FiniteDuration = 30.seconds

  def main(args: Array[String]) = {
    log.info("Starting the User Update Server...")

    // Loads the server certificate
    log.debug("Checking for the certificate.")
    if ( /* TODO, complete this certificate check */ ) {
      log.error("Server certificate could not be found.")
      return
    }

    implicit val system = ActorSystem(Behaviors.empty, "the-name-of-the-actor-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    // Starts the Http server
    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(Array(), new SecureRandom)
    val https: HttpsConnectionContext = ConnectionContext.httpsServer(sslContext)

    val serverInstance = Http().newServerAt(host, port)
    val bindingFuture: Future[Http.ServerBinding] = serverInstance.enableHttps().bind(UserUpdateRoute.UpdateRoute)
      .map(_.addToCoordinatedShutdown(hardTerminationDeadline = ShutdownTime))

    bindingFuture.failed.foreach{ ex =>
      log.error(f"Failed to bind $host to $port. Exception: $ex")
    }

    log.info("The server has been started.");

    waitUntilUserEndsServer();

    // Shuts down the server
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

    ClientDatabase.closeConnection()
    Producer.close()
    log.info("The server has been stopped.")
  }


  // TODO: Move this method into a common file for all servers to use.
  /**
   * Wait until the user ends the program to continue.
   */
  def waitUntilUserEndsServer(): Unit ={
    // Wait until the user stops the server
    running = true;
    while (running) {
      val std_input: String = StdIn.readLine().toLowerCase() // let it run until user presses return

      if (std_input == "exit" || std_input == "stop") {
        running = false;
      }else{
        println("Enter 'exit' or 'stop' to stop the server.")
      }
    }
  }
}

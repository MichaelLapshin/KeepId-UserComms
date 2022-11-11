package services.user_update

/**
 * @file: UserUpdateServer.scala
 * @description: The object in charge of starting and stopping the User Update Server.
 * @author: KeepId Inc.
 * @date: March 30, 2022
 */

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import common.client_database.ClientDatabase
import common.message_broker.Producer
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scala.io.StdIn

object UserUpdateServer {
  // Server variables
  private val log = Logger(getClass.getName)
  var running = false;
  val (interface: String, port: Int) = ("localhost", 8001)

  def main(args: Array[String]) = {
    log.info("Starting the User Update Server...")

    implicit val system = ActorSystem(Behaviors.empty, "the-name-of-the-actor-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    // Creates the objects needed to host and route the server
    val routes = new UserUpdateRoute();

    // Starts the Http server
    val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt(interface, port).bind(routes.UpdateRoute)
    log.info("The server has been started.");

    waitUntilUserEndsServer();

    // Terminates the server
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

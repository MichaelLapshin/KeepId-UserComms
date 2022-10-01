package service.user_response

/**
 * @file: UserResponseRoute.scala
 * @description: Define the class UserResponseRoute used in determining the route and logic of user responses.
 * @author: KeepId
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.{Connection, Domain, Producer}
import spray.json._
import service.user_response.UserResponseJsonProtocol.{
  userResponseAcceptReceiveFormat,
  userResponseAcceptForwardFormat,
  userResponseRejectReceiveFormat,
  userResponseReportReceiveFormat
}

class UserResponseRoute {

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  private def prepareAcceptMessage(): String = {
    // TODO: complete the logic here.
  }

  // Route definition
  lazy val responseRoute: Route = concat(
    get {
      // Alive route
      complete("I'm alive.") // TODO: generalize this response.
    },
    post {
      // User response route
      entity(as[]) { data => // TODO: create multiple entity post routes for accepting differnt types of respnoses.
        // Parses the response body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable("Invalid route.")) // TODO: generalize the error messages
  )

}

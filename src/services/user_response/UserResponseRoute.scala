package services.user_response

/**
 * @file: UserResponseRoute.scala
 * @description: Define the class UserResponseRoute used in determining the route and logic of user responses.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMessages}
import spray.json._
import services.user_response.{
  UserResponseAcceptReceiveData,
  UserResponseAcceptForwardData,
  UserResponseRejectReceiveData,
  UserIdManagerJsonProtocol
}

class UserResponseRoute extends Directives with UserIdManagerJsonProtocol {

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  private def prepareAcceptMessage(): String = {
    // TODO: complete the logic here.
  }

  // Route definition
  lazy val responseRoute: Route = concat(
    get {
      // Ping route
      complete(RouteReplyMessages.Ping)
    },
    post {
      // User response route
      entity(as[UserResponseAcceptReceiveData]) { data => // TODO: create multiple entity post routes for accepting differnt types of respnoses.
        // Parses the response body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable(RouteReplyMessages.InvalidRoute))
  )

}

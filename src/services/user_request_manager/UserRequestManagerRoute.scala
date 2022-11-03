package services.user_request_manager

/**
 * @file: UserRequestManagerRoute.scala
 * @description: Define the class UserRequestManagerRoute used in determining the route and logic of new request.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMsg}
import spray.json._
import services.user_request_manager.UserRequestManagerJsonProtocol.{

}

class UserRequestManagerRoute {

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  // Route definition
  lazy val requestFetchRoute: Route = concat(
    get {
      // Ping route
      complete(RouteReplyMsg.Ping)
    },
    post {
      // User request fetch route
      entity(as[]) { data =>
        // Parses the request fetch body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable(RouteReplyMsg.InvalidRoute))
  )

}

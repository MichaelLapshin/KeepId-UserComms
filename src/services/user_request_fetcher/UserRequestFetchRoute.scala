package services.user_request_fetcher

/**
 * @file: UserRequestFetchRoute.scala
 * @description: Define the class UserRequestFetchRoute used in determining the route and logic of user request fetching.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMessages}
import spray.json._
import services.user_request_fetcher.UserRequestFetchJsonProtocol.{
  userRequestFormat,
  userRequestFetchReceiveFormat,
  userRequestFetchReturnFormat
}

class UserRequestFetchRoute {

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  // Route definition
  lazy val requestFetchRoute: Route = concat(
    get {
      // Ping route
      complete(RouteReplyMessages.Ping)
    },
    post {
      // User request fetch route
      entity(as[]) { data =>
        // Parses the request fetch body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable(RouteReplyMessages.InvalidRoute))
  )

}

package services.user_id_manager

/**
 * @file: UserDataUpdateRoute.scala
 * @description: Define the class UserIdManagerRoute used in determining the route and logic of user ID creation.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMessages}
import spray.json._
import services.user_id_manager.UserIdManagerJsonProtocol.{userIdManagerReceiveFormat, userIdManagerReturnFormat}

class UserIdManagerRoute {

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  // Route definition
  lazy val idManagerRoute: Route = concat(
    get {
      // Ping route
      complete(RouteReplyMessages.Ping)
    },
    post {
      // User ID request route
      entity(as[userIdManagerReceiveFormat]) { data =>
        // Parses the request body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable(RouteReplyMessages.InvalidRoute))
  )

}

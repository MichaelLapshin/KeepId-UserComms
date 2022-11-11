package services.user_request_manager

/**
 * @file: UserRequestManagerRoute.scala
 * @description: Define the class UserRequestManagerRoute used in determining the route and logic of new request.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMsg}
import services.user_request_manager.{UserRequestManagerJsonProtocol, UserRequestReceiveData, UserRequestReturnData}

import com.typesafe.scalalogging.Logger
import spray.json._

class UserRequestManagerRoute extends Directives with UserRequestManagerJsonProtocol {
  private val log = Logger(getClass.getName)

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  // Route definition
  lazy val requestFetchRoute: Route = concat(
    get {
      // Ping route
      log.info("Received ping request.")
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

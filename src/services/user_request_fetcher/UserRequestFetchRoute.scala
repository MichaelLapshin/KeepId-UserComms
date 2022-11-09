package services.user_request_fetcher

/**
 * @file: UserRequestFetchRoute.scala
 * @description: Define the class UserRequestFetchRoute used in determining the route and logic of user request fetching.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMsg}
import org.slf4j.LoggerFactory
import spray.json._
import services.user_request_fetcher.{
  UserIdManagerJsonProtocol,
  UserRequestData,
  UserRequestFetchReceiveData,
  UserRequestFetchReturnData
}

class UserRequestFetchRoute extends Directives with UserIdManagerJsonProtocol {
  private val log = LoggerFactory.getLogger(this.getClass)

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

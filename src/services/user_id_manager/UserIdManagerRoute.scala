package services.user_id_manager

/**
 * @file: UserDataUpdateRoute.scala
 * @description: Define the class UserIdManagerRoute used in determining the route and logic of user ID creation.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directives._
import common.message_broker.{Connection, Producer}
import common.constants.{Domain, RouteReplyMsg}
import org.slf4j.LoggerFactory
import spray.json._
import services.user_id_manager.{UserIdManagerJsonProtocol, UserIdManagerReceiveData, UserIdManagerReturnData}

class UserIdManagerRoute extends Directives with UserIdManagerJsonProtocol {
  private val log = LoggerFactory.getLogger(this.getClass)

  private def prepareReturnMessage(): String = {
    // TODO: finish the logic here.
  }

  // Route definition
  lazy val idManagerRoute: Route = concat(
    get {
      // Ping route
      log.info("Received ping request.")
      complete(RouteReplyMsg.Ping)
    },
    post {
      // User ID request route
      entity(as[UserIdManagerReceiveData]) { data =>
        // Parses the request body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable(RouteReplyMsg.InvalidRoute))
  )

}

package service.user_request_fetcher

/**
 * @file: UserRequestFetchRoute.scala
 * @description: Define the class UserRequestFetchRoute used in determining the route and logic of user request fetching.
 * @author: KeepId
 * @date: September 29, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.{Connection, Domain, Producer}
import spray.json._
import service.user_request_fetcher.UserRequestFetchJsonProtocol.{
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
      // Alive route
      complete("I'm alive.")
    },
    post {
      // User request fetch route
      entity(as[]) { data =>
        // Parses the request fetch body
        // TODO: Complete this logic.
      }
    },
    failWith(new Throwable("Invalid route.")) // TODO: generalize the error messages
  )

}

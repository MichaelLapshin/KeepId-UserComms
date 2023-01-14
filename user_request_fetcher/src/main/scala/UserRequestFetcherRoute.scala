package keepid.services.user_request_fetcher

/**
 * @file: UserRequestFetcherRoute.scala
 * @description: Define the class UserRequestFetcherRoute used in determining the route and logic of user request fetching.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpProtocols, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.Logger
import keepid.common.authentication.DeviceAuth
import keepid.common.client_database.{ClientDatabase, DBRequestCollections}
import keepid.common.constants.HttpPaths
import keepid.common.database_structs.Request
import keepid.services.user_request_fetcher.{UserIdManagerJsonProtocol, UserRequestData, UserRequestFetchReceiveData, UserRequestFetchReturnData}

object UserRequestFetcherRoute extends Directives with UserIdManagerJsonProtocol {
  private val log = Logger(getClass.getName)

  // Route definition
  lazy val RequestFetchRoute: Route = concat(
    authenticateBasicAsync(realm = DeviceAuth.realm, DeviceAuth.authenticate) { user_id =>
      path(HttpPaths.UserRequestFetcher.GetRequestsActive) {
        get {
          // User active request fetch route
          entity(as[UserRequestFetchReceiveData]) { data =>
            try {
              val requests: List[Request] = DBRequestCollections.getUserActiveRequests(user_id)
              complete(
                HttpResponse(
                  status = StatusCodes.OK,
                  entity = HttpEntity(ContentTypes.`application/json`, requests.toString()),
                  protocol = HttpProtocols.`HTTP/2.0`
                )
              )
            } catch {
              case x: Throwable =>
                log.warn(s"Exception occurred with the following error: ${x}")
                ClientDatabase.rollback()
                complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
    },
    path(HttpPaths.Ping) {
      get {
        log.info("Received ping request.")
        complete(StatusCodes.OK)
      }
    }
  )

}

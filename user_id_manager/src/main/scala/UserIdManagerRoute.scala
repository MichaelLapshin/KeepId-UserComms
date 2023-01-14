package keepid.services.user_id_manager

/**
 * @file: UserDataUpdateRoute.scala
 * @description: Define the class UserIdManagerRoute used in determining the route and logic of user ID creation.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpProtocols, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.Logger
import keepid.common.client_database.{ClientDatabase, DBUserMap}
import keepid.common.constants.HttpPaths
import keepid.common.database_structs.User
import spray.json.enrichAny

object UserIdManagerRoute extends Directives with UserIdManagerJsonProtocol {
  private val log = Logger(getClass.getName)

  // Route definition
  lazy val IdManagerRoute: Route = concat(
    path(HttpPaths.UserIdManager.UserCreate) {
      post {
        entity(as[UserIdManagerReceiveData]) { data =>
          try {
            val new_user: User = DBUserMap.createUniqueUser("null" /* TODO, insert apple ID instead*/)
            val return_data = UserIdManagerReturnData(
              device_id = new_user.device_id,
              device_token = new_user.device_token,
              user_pin = new_user.user_pin
            )

            ClientDatabase.commit()
            complete(
              HttpResponse(
                status = StatusCodes.OK,
                entity = HttpEntity(ContentTypes.`application/json`, return_data.toJson.compactPrint),
                protocol = HttpProtocols.`HTTP/1.1`
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
    },
    path(HttpPaths.Ping) {
      get {
        log.info("Received ping request.")
        complete(StatusCodes.OK)
      }
    }
  )

}

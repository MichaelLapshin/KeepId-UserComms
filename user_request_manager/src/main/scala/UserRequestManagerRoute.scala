package keepid.services.user_request_manager

/**
 * @file: UserRequestManagerRoute.scala
 * @description: Define the class UserRequestManagerRoute used in determining the route and logic of new request.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import keepid.services.user_request_manager.{UserRequestManagerJsonProtocol, UserRequestReceiveData, UserRequestReturnData}
import com.typesafe.scalalogging.Logger
import keepid.common.authentication.CompanyHostAuth
import keepid.common.client_database.{ClientDatabase, DBRequestManager, DBSystemClientManager}
import keepid.common.constants.HttpPaths
import keepid.common.database_structs.Request

import java.time.{Duration, LocalDateTime}

object UserRequestManagerRoute extends Directives with UserRequestManagerJsonProtocol {
  private val log = Logger(getClass.getName)
  private val expireDuration = Duration.ofHours(24)

  // Route definition
  lazy val RequestManagerRoute: Route = concat(
    authenticateBasicAsync(realm = CompanyHostAuth.realm, CompanyHostAuth.authenticate) { company_id =>
      path(HttpPaths.UserRequestManager.InitiateDataRequest) {
        post {
          entity(as[UserRequestReceiveData]) { data =>
            try {
              // Create the request
              log.info("Creating a new request.")
              val create_time = LocalDateTime.now()
              val request = new Request(
                request_id = DBRequestManager.generateUniqueRequestId(),
                user_id = DBSystemClientManager.getUserId(data.user_pin),
                company_id = company_id,
                data_fields = data.expected_data_fields,
                active_time = create_time,
                response_time = null, // Empty value
                expire_time = create_time.plusMinutes(expireDuration.toMinutes)
              )

              // Store the request
              DBRequestManager.registerRequestToDB(request)
              ClientDatabase.commit()
              log.info(f"Created new request: $request.")

              // Notify the user that they have a new data request
              // TODO future: send push notification to the user here using UserRequestForwardData

              complete(StatusCodes.OK)
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

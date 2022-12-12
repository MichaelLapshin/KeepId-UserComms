package services.user_response

/**
 * @file: UserResponseRoute.scala
 * @description: Define the class UserResponseRoute used in determining the route and logic of user responses.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.Logger
import spray.json._
import com.typesafe.scalalogging.Logger
import common.authentication.{DeviceAuth, RequestAuth}
import common.client_database.{ClientDatabase, DBRequestManager}
import common.message_broker.{Connection, Producer, Topics}
import common.constants.{Domain, HttpPaths}
import services.user_response.{ResponseType, UserIdManagerJsonProtocol, UserResponseAcceptForwardData, UserResponseAcceptReceiveData, UserResponseRejectReceiveData, UserResponseReportReceiveData}

class UserResponseRoute extends Directives with UserIdManagerJsonProtocol {
  private[this] val log = Logger(getClass.getName)
  private[this] val user_response_producer = new Producer()

  // Route definition
  lazy val responseRoute: Route = concat(
    authenticateBasicAsync(realm = RequestAuth.realm, RequestAuth.authenticate) { request_id =>
      concat(
        path(HttpPaths.UserResponse.AcceptDataRequest) { // User accept response route
          post {
            entity(as[UserResponseAcceptReceiveData]) { data =>
              if (data.response == ResponseType.AcceptRequest) {
                try {
                  val expected_data_fields: Domain.ExpectedDataFields = DBRequestManager.getRequest(request_id).data_fields
                  DBRequestManager.acceptRequest(request_id)

                  val forward_data = UserResponseAcceptForwardData(
                    request_id = request_id,
                    expected_data_fields = expected_data_fields,
                    encrypted_public_keys = data.encrypted_public_keys,
                    encrypted_private_keys = data.encrypted_private_keys
                  )

                  if (user_response_producer.send(Topics.AcceptDataRequestTopic, forward_data.toJson.compactPrint)) {
                    ClientDatabase.commit()
                    complete(StatusCodes.OK)
                  } else {
                    log.warn(f"Failed to send the producer the message: $forward_data")
                    ClientDatabase.rollback()
                    complete(StatusCodes.InternalServerError)
                  }
                } catch {
                  case _: Throwable =>
                    log.error(s"Exception occurred with the following error: ${_}")
                    ClientDatabase.rollback()
                    complete(StatusCodes.InternalServerError)
                }
              } else {
                log.warn(f"Failed to accept the request due to the invalid response input. Data: $data")
                complete(StatusCodes.UnprocessableContent)
              }
            }
          }
        }
        ,
        path(HttpPaths.UserResponse.RejectDataRequest) { // User reject response route
          post {
            entity(as[UserResponseRejectReceiveData]) { data =>
              if (data.response == ResponseType.RejectRequest) {
                try {
                  DBRequestManager.rejectRequest(data.request_id)
                  ClientDatabase.commit()
                  complete(StatusCodes.OK)
                } catch {
                  case _: Throwable =>
                    log.error(f"Failed to reject the request. Response: $data.")
                    ClientDatabase.rollback()
                    complete(StatusCodes.InternalServerError)
                }
              } else {
                log.warn(f"Failed to reject the request due to the invalid response input. Data: $data")
                complete(StatusCodes.UnprocessableContent)
              }
            }
          }
        }
        ,
        path(HttpPaths.UserResponse.ReportDataRequest) { // User report response route
          post {
            entity(as[UserResponseReportReceiveData]) { data =>
              if (data.response == ResponseType.ReportRequest) {
                try {
                  DBRequestManager.reportRequest(data.request_id, data.report_message)
                  ClientDatabase.commit()
                  complete(StatusCodes.OK)
                } catch {
                  case _: Throwable =>
                    log.error(f"Failed to report the request with response $data due to the error: ${_}")
                    ClientDatabase.rollback()
                    complete(StatusCodes.InternalServerError)
                }
              } else {
                log.warn(f"Failed to report the request due to the invalid response input. Data: $data")
                complete(StatusCodes.UnprocessableContent)
              }
            }
          }
        }
      )
    },
    path(HttpPaths.Ping) {
      get {
        log.info("Received ping request.")
        complete(StatusCodes.OK)
      }
    }
  ) // end of route definition

}

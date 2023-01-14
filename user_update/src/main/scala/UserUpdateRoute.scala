package keepid.services.user_update

/**
 * @file: UserUpdateRoute.scala
 * @description: Define the class UserUpdateRoute used in determining
 *               the route and task of a user-data update request.
 * @author: KeepId Inc.
 * @date: April 10, 2022
 */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.Logger
import keepid.common.authentication.DeviceAuth
import keepid.common.constants.{Domain, HttpPaths}
import keepid.common.message_broker.{Producer, Topics}

object UserUpdateRoute extends Directives with UserUpdateJsonProtocol {
  private val log = Logger(getClass.getName)
  private val producer = new Producer()

  /**
   * Forward the message using the message broker to the Keep.
   *
   * @param user_id               The ID of the user.
   * @param encrypted_data_fields The encrypted data fields to forward.
   * @return True if the update message was successfully forwarded. False otherwise.
   */
  def updateUserData(user_id: Domain.UserId, encrypted_data_fields: Domain.EncryptedDataFields): Boolean = {
    // Using the Kafka stream, push data to the message broker
    log.debug(s"Pushing data to the message broker. With user id '$user_id' and encrypted data '$encrypted_data_fields'.")

    val dataToSend = UserUpdateForwardData(user_id, encrypted_data_fields)
    producer.send(Topics.UpdateDataTopic, dataToSend.toString)
  }

  // Route definition
  lazy val UpdateRoute: Route = concat(
    path(HttpPaths.UserUpdate.UpdateData) {
    authenticateBasicAsync(realm = DeviceAuth.realm, DeviceAuth.authenticate) { user_id =>
      post {
        // User update request route
        entity(as[UserUpdateReceiveData]) { data =>
          try {
            // Attempts to forward the data
            if (updateUserData(user_id, data.encrypted_data_fields)) {
              log.info(s"Forwarded the data update request for user with ID $user_id.")
              complete(StatusCodes.OK)
            } else {
              log.warn(s"Failed to forward the data update request. Data: $user_id.")
              complete(StatusCodes.InternalServerError)
            }
          } catch {
            case x: Throwable =>
              log.warn(f"Exception occurred when processing the data update request $data: ${x}")
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

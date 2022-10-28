package services.user_update

/**
 * @file: UserUpdateRoute.scala
 * @description: Define the class UserUpdateRoute used in determining
 *               the route and task of a user-data update request.
 * @author: KeepId Inc.
 * @date: April 10, 2022
 */

import akka.http.scaladsl.server.{Directives, Route}
import common.constants.{Domain, RouteReplyMessages}
import common.message_broker.{Connection, Producer}

class UserUpdateRoute extends Directives with UserUpdateJsonProtocol {
  /**
   * Fetches the user ID using the device ID from the database.
   *
   * @param device_id The device ID to use as reference.
   * @return The string user ID that is mapped from the given device ID.
   */
  def fetchUserId(device_id: Domain.DeviceId): Domain.UserId = {
    println(s"Fetching user id using the device id '$device_id'.")
    "some_user_id" // TODO, complete the logic here
  }

  /**
   * Forward the message using the message broker to the Keep.
   *
   * @param user_id               The ID of the user.
   * @param encrypted_data_fields The encrypted data fields to forward.
   * @return True if the update message was successfully forwarded. False otherwise.
   */
  def updateUserData(user_id: Domain.UserId, encrypted_data_fields: Domain.EncryptedDataFields): Boolean = {
    // Using the Kafka stream, push data to the message broker
    println(s"Pushing data to the message broker. With user id '$user_id' and encrypted data '$encrypted_data_fields'")

    val dataToSend = UserUpdateForwardData(user_id, encrypted_data_fields)
    Producer.send(Connection.Topic.KeepUpdateEntryTopic, dataToSend.toString)
  }

  // Route definition
  lazy val UpdateRoute: Route = concat(
    get {
      // Ping route
      complete(RouteReplyMessages.Ping)
    },
    post {
      // User update request route
      entity(as[UserUpdateReceiveData]) { data =>
        // Processes the request body information
        val user_id: Domain.UserId = fetchUserId(data.device_id)

        // Attempts to forward the data
        if (updateUserData(user_id, data.encrypted_data_fields)) {
          complete("Update request received.")
        }else {
          complete("Update request could not be processed.")
        }
      }
    },
    failWith(new Throwable(RouteReplyMessages.InvalidRoute))
  )

}

package service.user_update

/**
 * @file: UserUpdateRoute.scala
 * @description: Define the class UserUpdateRoute used in determining
 *               the route and task of a user-data update request.
 * @author: KeepId
 * @date: April 10, 2022
 */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import common.{Connection, Domains, Producer}
import spray.json._
import service.user_update.UserUpdateJsonProtocol.{userUpdateReceivedFormat, userUpdateToForwardFormat}

class UserUpdateRoute {
  /**
   * Fetches the user ID using the device ID from the database.
   *
   * @param device_id The device ID to use as reference.
   * @return The string user ID that is mapped from the given device ID.
   */
  def fetchUserId(device_id: Domain.DeviceId): Domain.UserId = {
    println(s"Fetching user id using the device id '$device_id'.")
    "some_user_id"
  }

  /**
   * Forward the message using the message broker to the Keep.
   *
   * @param user_id               The ID of the user.
   * @param encrypted_data_fields The encrypted datafields to forward.
   * @return True if the update message was successfully forwarded. False otherwise.
   */
  def updateUserData(user_id: Domain.UserId, encrypted_data_fields: Domain.EncryptedDataFields): Boolean = {
    // Using the Kafka stream, push data to the message broker
    println(s"Pushing data to the message broker. With user id '$user_id' and encrypted data '$encrypted_data_fields'")

    val dataToSend = UserUpdateToForward(user_id, encrypted_data_fields)
    Producer.send(Connection.Topic.KeepUpdateEntryTopic, dataToSend.toJson.prettyPrint)
  }

  private def prepareUpdateMessage(): String = {
    // TODO: complete the logic here.
  }

  // Route definition
  lazy val updateRoute: Route = concat(
    get {
      // Alive route
      complete("I'm alive.")
    },
    post {
      // User update request route
      entity(as[userUpdateReceiveFormat]) { data => // TODO: Make sure that the entity type is being used correctly
        // Parses the request body
        val content: UserUpdateReceived = data.parseJson.convertTo[UserUpdateReceived]

        // Processes the request body information
        val user_id: Domains.UserId = fetchUserId(content.device_id)

        // Attempts to forward the data
        if (updateUserData(user_id, content.encrypted_data_fields))
          complete("Update request received.")
        else
          complete("Update request could not be processed.")
      }
    },
    failWith(new Throwable("Invalid route."))
  )

}

package services.user_id_manager

/**
 * @file: UserIdManagerJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user ID managing sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import common.constants.Domain
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

// Defines the API of for receiving messages from a client user.
final case class UserIdManagerReceiveData() {}

// Defines the API for the return message to the client user.
final case class UserIdManagerReturnData(
                                          device_id: Domain.DeviceId,
                                          device_token: Domain.DeviceToken,
                                          user_pin: Domain.UserPin
                                        ) {}

// Enables the Json Protocol to implicitly interpret custom formats.
trait UserIdManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userIdManagerReceiveFormat = jsonFormat0(UserIdManagerReceiveData)
  implicit val userIdManagerReturnFormat = jsonFormat3(UserIdManagerReturnData)
}

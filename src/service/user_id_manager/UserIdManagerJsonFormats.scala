package service.user_id_manager

/**
 * @file: UserIdManagerJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user ID managing sequence.
 * @author: KeepId
 * @date: September 29, 2022
 */


import common.Domain
import spray.json.DefaultJsonProtocol

// Defines the API of for receiving messages from a client user.
case class UserIdManagerReceiveFormat(
                               user_certificate: Domain.UserCertificate,
                             )

// Defines the API for the return message to the client user.
case class UserIdManagerReturnFormat(
                                device_id: Domain.DeviceId,
                                user_pin: Domain.UserPin
                              )

// Enables the Json Protocol to implicitely intepret custom formats.
object UserIdManagerJsonProtocol extends DefaultJsonProtocol {
  implicit val userIdManagerReceiveFormat = jsonFormat1(UserIdManagerReceiveFormat)
  implicit val userIdManagerReturnFormat = jsonFormat2(UserIdManagerReturnFormat)
}

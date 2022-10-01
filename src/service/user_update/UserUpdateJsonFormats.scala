package service.user_update

/**
 * @file: UserUpdateJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user update sequence.
 * @author: KeepId
 * @date: April 10, 2022
 */


import common.Domain
import spray.json.DefaultJsonProtocol

// Class which defines the format and expected fields of the user update json.
case class UserUpdateReceiveFormat(
                               device_id: Domains.DeviceId,
                               encrypted_data_fields: Domains.EncryptedDataFields
                             )

// Class which defines the format and expected fields of the user update json
// to forward to the Keep.
case class UserUpdateForwardFormat(
                                user_id: Domains.UserId,
                                encrypted_data_fields: Domains.EncryptedDataFields
                              )

// Enables the Json Protocol to implicitely intepret custom formats.
object UserUpdateJsonProtocol extends DefaultJsonProtocol {
  implicit val userUpdateReceiveFormat = jsonFormat2(UserUpdateReceiveFormat)
  implicit val userUpdateForwardFormat = jsonFormat2(UserUpdateForwardFormat)
}

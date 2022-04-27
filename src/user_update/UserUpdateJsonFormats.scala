package user_update

/**
 * @file: UserUpdateJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings.
 * @author: KeepId
 * @date: April 10, 2022
 */


import common.Domains
import spray.json.DefaultJsonProtocol

// Class which defines the format and expected fields of the user update json.
case class UserUpdateReceived(
                               device_id: Domains.DeviceId,
                               encrypted_data_fields: Domains.EncryptedDataFields
                             )

// Class which defines the format and expected fields of the user update json
// to forward to the Keep.
case class UserUpdateToForward(
                                user_id: Domains.UserId,
                                encrypted_data_fields: Domains.EncryptedDataFields
                              )

object UserUpdateJsonProtocol extends DefaultJsonProtocol {
  implicit val userUpdateReceivedFormat = jsonFormat2(UserUpdateReceived)
  implicit val userUpdateToForwardFormat = jsonFormat2(UserUpdateToForward)
}

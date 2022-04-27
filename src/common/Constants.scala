package common

/**
 * @file: Constants.scala
 * @description: Define constants used within the User-Data Update server.
 * @author: KeepId
 * @date: April 10, 2022
 */

object Constants {
  val DeviceId: Domains.DeviceId = "device_id"
  val UserId: Domains.UserId = "user_id"
  val EncryptedDataFields: Domains.EncryptedDataFields = "encrypted_data_fields"
}

object Domains {
  type DeviceId = String
  type UserId = String
  type EncryptedDataFields = String
}

package services.user_update

/**
 * @file: UserUpdateJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user update sequence.
 * @author: KeepId Inc.
 * @date: April 10, 2022
 */

import common.constants.Domain
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

// Class which defines the format and expected fields of the user update json.
final case class UserUpdateReceiveData(
                                        encrypted_data_fields: Domain.EncryptedDataFields
                                      ) {}

// Class which defines the format and expected fields of the user update json
// to forward to the Keep.
final case class UserUpdateForwardData(
                                        user_id: Domain.UserId,
                                        encrypted_data_fields: Domain.EncryptedDataFields
                                      ) {}

// Enables the Json Protocol to implicitly interpret custom formats.
trait UserUpdateJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userUpdateReceiveFormat = jsonFormat1(UserUpdateReceiveData)
  implicit val userUpdateForwardFormat = jsonFormat2(UserUpdateForwardData)
}

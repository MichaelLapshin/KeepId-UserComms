package services.encrypted_data_sender

/**
 * @file: EncryptedDataSenderJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the encrypted data sender sequence.
 * @author: KeepId Inc.
 * @date: October 1, 2022
 */

import common.constants.Domain
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

// API for obtaining encrypted data fields from the Keep
final case class EncryptedDataReceiveData(
                                           request_id: Domain.RequestId,
                                           encrypted_data_fields: Domain.EncryptedDataFields
                                         )

// API for returning the user's requests
final case class EncryptedDataForwardData(
                                           request_id: Domain.RequestId,
                                           encrypted_data_fields: Domain.EncryptedDataFields
                                         )

// Enables the Json Protocol to implicitly interpret custom formats.
trait EncryptedDataSenderJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val encryptedDataReceiveFormat = jsonFormat2(EncryptedDataReceiveData)
  implicit val encryptedDataForwardFormat = jsonFormat2(EncryptedDataForwardData)
}

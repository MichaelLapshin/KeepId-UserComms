package service.encrypted_data_sender

/**
 * @file: EncryptedDataSenderJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the encrypted data sender sequence.
 * @author: KeepId
 * @date: October 1, 2022
 */

import common.Domain

// API for obtaining encrypted data fields from the Keep
case class EncryptedDataReceiveFormat(
                                       request_id: Domain.RequestId,
                                       encrypted_data_fields: Domain.EncryptedDataFields
                                     )

// API for returning the user's requests
case class EncryptedDataForwardFormat(
                                       request_id: Domain.RequestId,
                                       encrypted_data_fields: Domain.EncryptedDataFields
                                     )

// Enables the Json Protocol to implicitly interpret custom formats.
object EncryptedDataSenderJsonProtocol extends DefaultJsonProtocol {
  implicit val encryptedDataReceiveFormat = jsonFormat2(EncryptedDataReceiveFormat)
  implicit val encryptedDataForwardFormat = jsonFormat2(EncryptedDataForwardFormat)
}

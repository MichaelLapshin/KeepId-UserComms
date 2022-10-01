package service.user_response

/**
 * @file: UserResponseJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user response sequence.
 * @author: KeepId
 * @date: September 29, 2022
 */


import common.Domain
import spray.json.DefaultJsonProtocol

// API for accept reponses from the user
case class UserResponseAcceptReceiveFormat(
                               request_id: Domain.RequestId,
                               response: Domain.Response,
                               encrypted_public_keys: Domain.EncryptedPublicKeys,
                               encrypted_private_keys: Domain.EncryptedPrivateKeys
                             )

case class UserResponseAcceptForwardFormat(
                                            request_id: Domain.RequestId,
                                            response: Domain.Response,
                                            expected_data_fields: Domain.ExpectedDataFields,
                                            encrypted_public_keys: Domain.EncryptedPublicKeys,
                                            encrypted_private_keys: Domain.EncryptedPrivateKeys
                                          )

// API for reject resposes from the user
case class UserResponseRejectReceiveFormat(
                                request_id: Domain.RequestId,
                                response: Domain.Response
                              )

// API for report responses from the user
case class UserResponseReportReceiveFormat(
                                request_id: Domain.RequestId,
                                response: Domain.Response
                                report_message: Domain.ReportMessage
                              )


// Enables the Json Protocol to implicitely intepret custom formats.
object UserIdManagerJsonProtocol extends DefaultJsonProtocol {
  implicit val userResponseAcceptReceiveFormat = jsonFormat4(UserResponseAcceptReceiveFormat)
  implicit val userResponseAcceptForwardFormat = jsonFormat5(UserResponseAcceptForwardFormat)
  implicit val userResponseRejectReceiveFormat = jsonFormat2(UserResponseRejectReceiveFormat)
  implicit val userResponseReportReceiveFormat = jsonFormat3(UserResponseReportReceiveFormat)
}

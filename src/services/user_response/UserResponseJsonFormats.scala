package services.user_response

/**
 * @file: UserResponseJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user response sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import common.constants.Domain
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

// API for accept responses from the user
final case class UserResponseAcceptReceiveData(
                                                request_id: Domain.RequestId,
                                                response: Domain.Response,
                                                encrypted_public_keys: Domain.EncryptedPublicKeys,
                                                encrypted_private_keys: Domain.EncryptedPrivateKeys
                                              )

final case class UserResponseAcceptForwardData(
                                                request_id: Domain.RequestId,
                                                response: Domain.Response,
                                                expected_data_fields: Domain.ExpectedDataFields,
                                                encrypted_public_keys: Domain.EncryptedPublicKeys,
                                                encrypted_private_keys: Domain.EncryptedPrivateKeys
                                              )

// API for reject responses from the user
final case class UserResponseRejectReceiveData(
                                                request_id: Domain.RequestId,
                                                response: Domain.Response
                                              )

// API for report responses from the user
final case class UserResponseReportReceiveData(
                                                request_id: Domain.RequestId,
                                                response: Domain.Response,
                                                report_message: Domain.ReportMessage
                                              )


// Enables the Json Protocol to implicitly interpret custom formats.
trait UserIdManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userResponseAcceptReceiveFormat = jsonFormat4(UserResponseAcceptReceiveData)
  implicit val userResponseAcceptForwardFormat = jsonFormat5(UserResponseAcceptForwardData)
  implicit val userResponseRejectReceiveFormat = jsonFormat2(UserResponseRejectReceiveData)
  implicit val userResponseReportReceiveFormat = jsonFormat3(UserResponseReportReceiveData)
}

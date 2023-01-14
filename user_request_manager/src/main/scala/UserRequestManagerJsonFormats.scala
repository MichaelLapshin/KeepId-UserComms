package keepid.services.user_request_manager

/**
 * @file: UserRequestManagerJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the request managing sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import keepid.common.constants.Domain
import spray.json.DefaultJsonProtocol

// API for fetch request from the user
final case class UserRequestReceiveData(
                                         user_pin: Domain.UserPin,
                                         expected_data_fields: Domain.ExpectedDataFields
                                       ) {}

// API for returning the user's requests
final case class UserRequestReturnData(request_id: Domain.RequestId) {}

final case class UserRequestForwardData(
                                         company_name: Domain.CompanyName,
                                         company_pin: Domain.CompanyPin,
                                         request_id: Domain.RequestId,
                                         expected_data_fields: Domain.ExpectedDataFields
                                       ) {}

// Enables the Json Protocol to implicitly interpret custom formats.
trait UserRequestManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userRequestReceiveFormat = jsonFormat2(UserRequestReceiveData)
  implicit val userRequestReturnFormat = jsonFormat1(UserRequestReturnData)
  implicit val userRequestForwardFormat = jsonFormat4(UserRequestForwardData)
}

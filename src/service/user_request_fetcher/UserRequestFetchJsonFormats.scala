package service.user_request_fetcher

/**
 * @file: UserRequestFetchJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user request fetch sequence.
 * @author: KeepId
 * @date: September 29, 2022
 */


import common.Domain
import spray.json.DefaultJsonProtocol

// Json format for handling an individual request
case class UserRequestFormat(
                              request_id: Domain.RequestId,
                              company_id: Domain.CompanyId,
                              data_fields: List[Domain.DataField],
                              active_from: Time,
                              response_time: Time,
                              expire_time: Time
                            )

// API for fetch request from the user
case class UserRequestFetchReceiveFormat(
                                          device_id: Domain.DeviceId,
                                        )

// API for returning the user's requests
case class UserRequestFetchReturnFormat(
                                         requests: List[UserRequestFormat],
                                       )

// Enables the Json Protocol to implicitely intepret custom formats.
object UserIdManagerJsonProtocol extends DefaultJsonProtocol {
  implicit val userRequestFetchReceiveFormat = jsonFormat1(UserRequestFetchReceiveFormat)
  implicit val userRequestFetchReturnFormat = jsonFormat1(UserRequestFetchReturnFormat)
  implicit val userRequestFormat = jsonFormat6(UserRequestFormat)
}

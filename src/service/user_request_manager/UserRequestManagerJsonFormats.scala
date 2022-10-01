package service.user_request_manager

/**
 * @file: UserRequestManagerJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the request managing sequence.
 * @author: KeepId
 * @date: September 29, 2022
 */


import common.Domain
import spray.json.DefaultJsonProtocol

// API for fetch request from the user
case class UserRequestReceiveFormat(
                                     user_pin: Domain.UserPin,
                                     data_fields: List[Domain.DataField]
                                   )

// API for returning the user's requests
case class UserRequestReturnFormat(
                                    request_id: Domain.RequestId
                                  )

// Enables the Json Protocol to implicitely intepret custom formats.
object UserRequestManagerJsonProtocol extends DefaultJsonProtocol {
  implicit val userRequestReceiveFormat = jsonFormat2(UserRequestReceiveFormat)
  implicit val userRequestReturnFormat = jsonFormat1(UserRequestReturnFormat)
}

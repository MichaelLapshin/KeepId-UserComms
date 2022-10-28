package services.user_request_fetcher

/**
 * @file: UserRequestFetchJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user request fetch sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import java.time._
import spray.json.{DefaultJsonProtocol, JsObject, JsValue, RootJsonFormat, enrichAny}
import common.constants.{Domain, ProtocolJsonKeys, Time}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

// Json format for handling an individual request
final case class UserRequestData(
                                  request_id: Domain.RequestId,
                                  company_id: Domain.CompanyId,
                                  company_name: Domain.CompanyName,
                                  expected_data_fields: Domain.ExpectedDataFields,
                                  active_time: LocalDateTime,
                                  response_time: LocalDateTime,
                                  expire_time: LocalDateTime
                                )

// API for fetch request from the user
final case class UserRequestFetchReceiveData(device_id: Domain.DeviceId)

// API for returning the user's requests
final case class UserRequestFetchReturnData(requests: List[UserRequestData])

// Enables the Json Protocol to implicitly interpret custom formats.
trait UserIdManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userRequestFetchReceiveFormat = jsonFormat1(UserRequestFetchReceiveData)

  implicit object userRequestFetchReturnFormat extends RootJsonFormat[UserRequestFetchReturnData] {
    def read(value: JsValue) = UserRequestFetchReturnData(value.convertTo[List[UserRequestData]])

    def write(f: UserRequestFetchReturnData): JsValue = JsObject(ProtocolJsonKeys.Requests -> f.requests.toJson)
  }

  implicit object userRequestFormat extends RootJsonFormat[UserRequestData] {
    def read(value: JsValue) = UserRequestData(
      request_id = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.RequestId, null).toString,
      company_id = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.CompanyId, null).toString,
      company_name = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.CompanyName, null).toString,
      expected_data_fields = value.asJsObject.fields.get(ProtocolJsonKeys.ExpectedDataFields).toJson.convertTo[Domain.ExpectedDataFields],
      active_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ActiveTime, Time.NullTime).toString, Time.StringFormat),
      response_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ResponseTime, Time.NullTime).toString, Time.StringFormat),
      expire_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ExpireTime, Time.NullTime).toString, Time.StringFormat)
    )

    def write(f: UserRequestData): JsValue = JsObject(
      ProtocolJsonKeys.RequestId -> f.request_id.toJson,
      ProtocolJsonKeys.CompanyId -> f.company_id.toJson,
      ProtocolJsonKeys.CompanyName -> f.company_name.toJson,
      ProtocolJsonKeys.ExpectedDataFields -> f.expected_data_fields.toJson,
      ProtocolJsonKeys.ActiveTime -> f.active_time.format(Time.StringFormat).toJson,
      ProtocolJsonKeys.ResponseTime -> f.response_time.format(Time.StringFormat).toJson,
      ProtocolJsonKeys.ExpireTime -> f.expire_time.format(Time.StringFormat).toJson
    )
  }
}



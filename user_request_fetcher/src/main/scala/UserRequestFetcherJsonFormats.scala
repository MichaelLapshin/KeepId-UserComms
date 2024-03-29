package keepid.services.user_request_fetcher

/**
 * @file: UserRequestFetcherJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user request fetch sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import keepid.common.constants.{Domain, ProtocolJsonKeys, Time}
import spray.json.{DefaultJsonProtocol, JsObject, JsValue, RootJsonFormat, enrichAny}

import java.time._

// Json format for handling an individual request
final case class UserRequestData(
                                  request_id: Domain.RequestId,
                                  company_id: Domain.CompanyId,
                                  company_name: Domain.CompanyName,
                                  expected_data_fields: Domain.ExpectedDataFields,
                                  active_time: LocalDateTime,
                                  response_time: LocalDateTime,
                                  expire_time: LocalDateTime
                                ) {}

// API for fetch request from the user
final case class UserRequestFetchReceiveData() {}

// API for returning the user's requests
final case class UserRequestFetchReturnData(requests: List[UserRequestData]) {}

// Enables the Json Protocol to implicitly interpret custom formats.
trait UserIdManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object userRequestFormat extends RootJsonFormat[UserRequestData] {
    override def read(value: JsValue) = UserRequestData(
      request_id = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.RequestId, null).convertTo[Long],
      company_id = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.CompanyId, null).convertTo[Long],
      company_name = value.asJsObject.fields.getOrElse(ProtocolJsonKeys.CompanyName, null).toString,
      expected_data_fields = value.asJsObject.fields.get(ProtocolJsonKeys.ExpectedDataFields).toJson.convertTo[Domain.ExpectedDataFields],
      active_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ActiveTime, Time.NullTime).toString, Time.StringFormat),
      response_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ResponseTime, Time.NullTime).toString, Time.StringFormat),
      expire_time = LocalDateTime.parse(value.asJsObject.fields.getOrElse(ProtocolJsonKeys.ExpireTime, Time.NullTime).toString, Time.StringFormat)
    )

    override def write(f: UserRequestData): JsValue = JsObject(
      ProtocolJsonKeys.RequestId -> f.request_id.toJson,
      ProtocolJsonKeys.CompanyId -> f.company_id.toJson,
      ProtocolJsonKeys.CompanyName -> f.company_name.toJson,
      ProtocolJsonKeys.ExpectedDataFields -> f.expected_data_fields.toJson,
      ProtocolJsonKeys.ActiveTime -> f.active_time.format(Time.StringFormat).toJson,
      ProtocolJsonKeys.ResponseTime -> f.response_time.format(Time.StringFormat).toJson,
      ProtocolJsonKeys.ExpireTime -> f.expire_time.format(Time.StringFormat).toJson
    )
  }

  implicit val userRequestFetchReceiveFormat = jsonFormat0(UserRequestFetchReceiveData)

  implicit object userRequestFetchReturnFormat extends RootJsonFormat[UserRequestFetchReturnData] {
    override def read(value: JsValue) = UserRequestFetchReturnData(value.convertTo[List[UserRequestData]])
    override def write(f: UserRequestFetchReturnData): JsValue = JsObject(ProtocolJsonKeys.Requests -> f.requests.toJson)
  }
}



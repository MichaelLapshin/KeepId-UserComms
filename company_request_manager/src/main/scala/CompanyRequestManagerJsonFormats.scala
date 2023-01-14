package keepid.services.company_request_manager

/**
 * @file: UserIdManagerJsonFormats.scala
 * @description: Define the formats of valid received and sent json strings in the user ID managing sequence.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import keepid.common.constants.Domain
import spray.json.DefaultJsonProtocol

// Defines the API for obtaining the private key from the Keep.
final case class CompanyRequestManagerReceiveData(
                                                   request_id: Domain.RequestId,
                                                   private_key: Domain.PrivateKey
                                                 ) {}

// Defines the API of for receiving messages from a company host server.
final case class CompanyRequestManagerHostReceiveData(request_id: Domain.RequestId) {}

// Defines the API for the return message to a company host server.
final case class CompanyRequestManagerHostReturnData(
                                                      request_id: Domain.RequestId,
                                                      private_key: Domain.PrivateKey
                                                    ) {}

// Enables the Json Protocol to implicitly interpret custom formats.
trait CompanyRequestManagerJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val companyRequestManagerReceiveFormat = jsonFormat2(CompanyRequestManagerReceiveData)
  implicit val companyRequestManagerHostReceiveFormat = jsonFormat1(CompanyRequestManagerHostReceiveData)
  implicit val companyRequestManagerHostReturnFormat = jsonFormat2(CompanyRequestManagerHostReturnData)
}

package common.constants

/**
 * @file: ProtocolJsonKeys.scala
 * @description: Define the names of the keys used within the Json protocols used to communicate between services.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

object ProtocolJsonKeys {
  // User information
  val UserId: String = "user_id"
  val UserPin: String = "user_pin"
  val DeviceId: String = "device_id"
  val DeviceToken: String = "device_token"

  // Data fields
  val EncryptedDataFields: String = "encrypted_data_fields"
  val ExpectedDataFields: String = "expected_data_fields"

  // Response data
  val Requests: String = "requests"
  val Response: String = "response"
  val RequestId: String = "request_id"
  val ReportMessage: String = "report_message"

  // Encryption data
  val EncryptedPublicKeys: String = "encrypted_public_keys"
  val EncryptedPrivateKeys: String = "encrypted_private_keys"

  // Company information
  val CompanyId: String = "company_id"
  val CompanyPin: String = "company_pin"
  val CompanyPassword: String = "company_password"
  val CompanyName: String = "company_name"
  val CompanyAddress: String = "company_address"
  val CompanyHostId: String = "company_host_id"
  val CompanyHostUrl: String = "company_host_url"
  val CompanyHostToken: String = "company_host_token"

  // Request time
  val ActiveTime: String = "active_time"
  val ResponseTime: String = "response_time"
  val ExpireTime: String = "expire_time"
}


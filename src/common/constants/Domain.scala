package common.constants

/**
 * @file: Domain.scala
 * @description: Define domains for the backend services.
 * @author: KeepId
 * @date: October 13, 2022
 */

object Domain {
  // User information
  type DeviceId = String
  type UserId = String
  type UserPin = String
  type UserCertificate = String

  // Data fields
  type DataField = String
  type EncryptedDataFields = String
  type ExpectedDataFields = List[String]

  // Response data
  type Response = String
  type RequestId = String
  type ReportMessage = String

  // Encryption data
  type EncryptedPublicKeys = String
  type EncryptedPrivateKeys = String
  type PrivateKey = String
  type PublicKey = String

  // Company information
  type HostAddress = String
  type HostCertificate = String
  type CompanyName = String
}
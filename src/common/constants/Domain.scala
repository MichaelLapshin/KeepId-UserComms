package common.constants

/**
 * @file: Domain.scala
 * @description: Define domains for the backend services.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

object Domain {
  // User information
  type UserId = String
  type UserPin = String
  type DeviceId = String
  type UserCertificate = String

  // User device provider identification
  type AppleId = String

  // Data fields
  type DataField = String
  type EncryptedDataFields = String
  type ExpectedDataFields = List[DataField]

  // Response data
  type Response = String
  type RequestId = String
  type ReportMessage = String

  // Encryption data
  type PublicKey = String
  type PrivateKey = String
  type EncryptedPublicKeys = String
  type EncryptedPrivateKeys = String

  // Company information
  type CompanyId = String
  type CompanyName = String
  type CompanyAddress = String
  type HostAddress = String
  type HostCertificate = String
}
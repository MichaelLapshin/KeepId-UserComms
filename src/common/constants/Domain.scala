package common.constants

/**
 * @file: Domain.scala
 * @description: Define domains for the backend services.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

object Domain {
  // User information
  type UserId = Long
  type UserPin = String
  type DeviceId = Long
  type DeviceToken = String

  // User device provider identification
  type AppleId = String

  // Data fields
  type DataField = String
  type EncryptedDataFields = String
  type ExpectedDataFields = List[DataField]

  // Response data
  type Response = String
  type RequestId = Long
  type ReportMessage = String

  // Encryption data
  type PublicKey = String
  type PrivateKey = String
  type EncryptedPublicKeys = String
  type EncryptedPrivateKeys = String

  // Company information
  type CompanyId = Long
  type CompanyPin = String
  type CompanyPassword = String
  type CompanyName = String
  type CompanyAddress = String
  type CompanyHostId = Long
  type CompanyHostUrl = String
  type CompanyHostToken = String
}
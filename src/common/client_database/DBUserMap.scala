package common.client_database

/**
 * @file: DBUserMap.scala
 * @description: Define the methods for generating new unique users.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain
import common.database_structs.UserIdMap

object DBUserMap {
  private val random = new scala.util.Random()
  private val randomRetryLimit: Int = 1000
  private val userPinLength: Int = 6
  private val validUserPinChars: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

  /**
   * generateAlphaNumStr()
   * Generates a string of the given length using only capital characters and integers.
   * @param length The length of the string to generate.
   * @return The generated capitalized alpha-numeric string.
   */
  private def generateAlphaNumStr(length: Int): String = random.alphanumeric.filter(c => validUserPinChars.contains(c)).take(length).mkString

  /**
   * generateUniqueUserId()
   * Generates a random unique user_id.
   *
   * @return Long integer representing the unique user_id.
   */
  private def generateUniqueUserId(): Domain.UserId = {
    var attempts: Int = randomRetryLimit
    while (attempts > 0) {
      attempts -= 1

      // Generates a random user_id and validates its existence
      val user_id: Domain.UserId = random.nextLong().abs
      if (DBSystemClientManager.existsUserId(user_id) == false) {
        return user_id
      }
    }
    throw new Throwable(f"Failed to generate a unique user_id within ${randomRetryLimit} attempts.")
  }

  /**
   * generateUniqueUserPin()
   * Generates a random unique user_pin.
   *
   * @return String of fixed length representing the unique user_pin.
   */
  private def generateUniqueUserPin(): Domain.UserPin = {
    var attempts: Int = randomRetryLimit
    while (attempts > 0) {
      attempts -= 1

      // Generates a random user_pin and validates its existence
      val user_pin: String = generateAlphaNumStr(userPinLength)
      if (DBSystemClientManager.existsUserPin(user_pin) == false) {
        return user_pin
      }
    }
    throw new Throwable(f"Failed to generate a unique user_pin within ${randomRetryLimit} attempts.")
  }

  /**
   * generateUniqueDeviceId()
   * Generates a random unique device_id.
   *
   * @return Long integer representing the unique device_id.
   */
  private def generateUniqueDeviceId(): Domain.DeviceId = {
    var attempts: Int = randomRetryLimit
    while (attempts > 0) {
      attempts -= 1

      // Generates a random device_id and validates its existence
      val device_id: Domain.DeviceId = random.nextLong().abs
      if (DBSystemClientManager.existsDeviceId(device_id) == false) {
        return device_id
      }
    }
    throw new Throwable(f"Failed to generate a unique device_id within ${randomRetryLimit} attempts.")
  }

  /**
   * createUniqueUser()
   * Create a unique user. This includes the userId, userPin, and deviceId.
   *
   * @return A structure mapping the 3 unique identifiers of the user.
   */
  def createUniqueUser() = new UserIdMap(
    user_id = generateUniqueUserId(),
    user_pin = generateUniqueUserPin(),
    device_id = generateUniqueDeviceId()
  )

  /**
   * registerUserToDB()
   * Stored the new unique user mapping in the client database.
   */
  def registerUserToDB(user_id_map: UserIdMap): Unit = {
    // TODO, complete this logic
    DatabaseIO.commit()
  }
}

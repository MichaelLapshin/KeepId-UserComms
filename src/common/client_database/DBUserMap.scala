package common.client_database

/**
 * @file: DBUserMap.scala
 * @description: Define the methods for generating new unique users.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain
import common.database_structs.UserIdMap
import org.slf4j.LoggerFactory

import java.sql.{PreparedStatement, Timestamp}
import java.time.LocalDateTime

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBUserMap {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val random = new scala.util.Random()
  private val randomRetryLimit: Int = 1000
  private val userPinLength: Int = 20
  private val validUserPinChars: String = "abcdefghijklmnopqrstuvwxyz0123456789"

  /**
   * generateAlphaNumStr()
   * Generates a string of the given length using only capital characters and integers.
   *
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
      log.debug(s"Attempting to create a unique user ID. Attempt: ${randomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random user_id and validates its existence
      val user_id: Domain.UserId = random.nextLong().abs
      if (DBSystemClientManager.existsUserId(user_id) == false) {
        log.info(s"Found unique user ID ${user_id} after ${randomRetryLimit - attempts} attempts.")
        return user_id
      }
    }
    log.warn(f"Failed to generate a unique user_id within ${randomRetryLimit} attempts.")
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
      log.debug(s"Attempting to create a unique user PIN. Attempt: ${randomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random user_pin and validates its existence
      val user_pin: String = generateAlphaNumStr(userPinLength)
      if (DBSystemClientManager.existsUserPin(user_pin) == false) {
        log.info(s"Found unique user PIN ${user_pin} after ${randomRetryLimit - attempts} attempts.")
        return user_pin
      }
    }
    log.warn(f"Failed to generate a unique user_pin within ${randomRetryLimit} attempts.")
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
      log.debug(s"Attempting to create a unique device ID. Attempt: ${randomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random device_id and validates its existence
      val device_id: Domain.DeviceId = random.nextLong().abs
      if (DBSystemClientManager.existsDeviceId(device_id) == false) {
        log.info(s"Found unique device ID ${device_id} after ${randomRetryLimit - attempts} attempts.")
        return device_id
      }
    }
    log.warn(f"Failed to generate a unique device_id within ${randomRetryLimit} attempts.")
    throw new Throwable(f"Failed to generate a unique device_id within ${randomRetryLimit} attempts.")
  }

  /**
   * registerUserToDB()
   * Stored the new unique user mapping in the client database.
   */
  private def registerUserToDB(user: UserIdMap,
                               user_certificate: String,
                               apple_id: String): Unit = {
    log.info(s"Registering new user ${user} with user_certificate=${user_certificate}, apple_id=${apple_id}.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement(
      "INSERT INTO User (user_id, device_id, user_pin, user_certificate, apple_id) VALUES (?, ?, ?, ?, ?, ?)"
    )
    ps.setLong(1, user.user_id)
    ps.setLong(2, user.device_id)
    ps.setNString(3, user.user_pin)
    ps.setString(4, user_certificate)
    ps.setString(5, apple_id)
    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()))

    ps.execute()
  }

  /**
   * createUniqueUser()
   * Create a unique user. This includes the userId, userPin, and deviceId.
   *
   * @return A structure mapping the 3 unique identifiers of the user.
   */
  def createUniqueUser(user_certificate: String, apple_id: String) = {
    log.debug("Attempting to create a unique user.")
    val user = new UserIdMap(
      user_id = generateUniqueUserId(),
      user_pin = generateUniqueUserPin(),
      device_id = generateUniqueDeviceId()
    )
    log.info(s"Created new user ${user}")
    registerUserToDB(user, user_certificate, apple_id)
  }
}

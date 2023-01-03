package common.client_database

/**
 * @file: DBUserMap.scala
 * @description: Define the methods for generating new unique users.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain
import common.database_structs.User
import com.typesafe.scalalogging.Logger

import java.sql.{PreparedStatement, Timestamp}
import java.time.LocalDateTime
import java.security.SecureRandom

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBUserMap {
  private val log = Logger(getClass.getName)

  // For creating IDs
  private val random = new scala.util.Random()
  private val RandomRetryLimit: Int = 1000
  private val UserPinLength: Int = 20
  private val UserPinChars: String = "abcdefghijklmnopqrstuvwxyz0123456789"

  // For creating tokens
  private val secureRandom = new SecureRandom()
  private val DeviceTokenLength: Int = 24
  private val DeviceTokenChars: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"


  /**
   * generateAlphaNumStr()
   * Generates a string of the given length using only capital characters and integers.
   *
   * @param length The length of the string to generate.
   * @return The generated capitalized alpha-numeric string.
   */
  private def generateAlphaNumStr(length: Int): String = random.alphanumeric.filter(c => UserPinChars.contains(c)).take(length).mkString

  /**
   * generateUniqueUserId()
   * Generates a random unique user_id.
   *
   * @return Long integer representing the unique user_id.
   */
  private def generateUniqueUserId(): Domain.UserId = {
    var attempts: Int = RandomRetryLimit
    while (attempts > 0) {
      log.debug(s"Attempting to create a unique user ID. Attempt: ${RandomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random user_id and validates its existence
      val user_id: Domain.UserId = random.nextLong().abs
      if (DBSystemClientManager.existsUserId(user_id) == false) {
        log.info(s"Found unique user ID ${user_id} after ${RandomRetryLimit - attempts} attempts.")
        return user_id
      }
    }
    log.warn(f"Failed to generate a unique user_id within $RandomRetryLimit attempts.")
    throw new Throwable(f"Failed to generate a unique user_id within $RandomRetryLimit attempts.")
  }

  /**
   * generateUniqueUserPin()
   * Generates a random unique user_pin.
   *
   * @return String of fixed length representing the unique user_pin.
   */
  private def generateUniqueUserPin(): Domain.UserPin = {
    var attempts: Int = RandomRetryLimit
    while (attempts > 0) {
      log.debug(s"Attempting to create a unique user PIN. Attempt: ${RandomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random user_pin and validates its existence
      val user_pin: String = generateAlphaNumStr(UserPinLength)
      if (DBSystemClientManager.existsUserPin(user_pin) == false) {
        log.info(s"Found unique user PIN ${user_pin} after ${RandomRetryLimit - attempts} attempts.")
        return user_pin
      }
    }
    log.warn(f"Failed to generate a unique user_pin within $RandomRetryLimit attempts.")
    throw new Throwable(f"Failed to generate a unique user_pin within $RandomRetryLimit attempts.")
  }

  /**
   * generateUniqueDeviceId()
   * Generates a random unique device_id.
   *
   * @return Long integer representing the unique device_id.
   */
  private def generateUniqueDeviceId(): Domain.DeviceId = {
    var attempts: Int = RandomRetryLimit
    while (attempts > 0) {
      log.debug(s"Attempting to create a unique device ID. Attempt: ${RandomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random device_id and validates its existence
      val device_id: Domain.DeviceId = random.nextLong().abs
      if (DBSystemClientManager.existsDeviceId(device_id) == false) {
        log.info(s"Found unique device ID ${device_id} after ${RandomRetryLimit - attempts} attempts.")
        return device_id
      }
    }
    log.warn(f"Failed to generate a unique device_id within $RandomRetryLimit attempts.")
    throw new Throwable(f"Failed to generate a unique device_id within $RandomRetryLimit attempts.")
  }

  /**
   * generateDeviceToken()
   * Generates a random new secure device token.
   *
   * @return String representing the device token.
   */
  private def generateDeviceToken(): Domain.DeviceToken = {
    val charLen = DeviceTokenChars.length()

    def generateTokenAccumulator(accumulator: String, number: Int): String = {
      if (number == 0) accumulator
      else generateTokenAccumulator(accumulator + DeviceTokenChars(secureRandom.nextInt(charLen)).toString, number - 1)
    }

    generateTokenAccumulator("", DeviceTokenLength)
  }

  /**
   * registerUserToDB()
   * Stored the new unique user mapping in the client database.
   */
  private def registerUserToDB(user: User): Unit = {
    log.info(s"Registering into the Client Database the new user $user.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement(
      "INSERT INTO User (user_id, user_pin, device_id, device_token, apple_id, registration_time) VALUES (?, ?, ?, ?, ?, ?)"
    )
    ps.setLong(1, user.user_id)
    ps.setNString(2, user.user_pin)
    ps.setLong(3, user.device_id)
    ps.setString(4, user.device_token)
    ps.setString(5, user.apple_id)
    ps.setTimestamp(6, Timestamp.valueOf(user.registration_time))
    ps.execute()
  }

  /**
   * createUniqueUser()
   * Create a unique user. This includes the userId, userPin, and deviceId.
   *
   * @param apple_id , the apple ID of the user.
   * @return A structure mapping the 3 unique identifiers of the user.
   */
  def createUniqueUser(apple_id: Domain.AppleId): User = {
    log.debug("Attempting to create a unique user.")
    val user = new User(
      user_id = generateUniqueUserId(),
      user_pin = generateUniqueUserPin(),
      device_id = generateUniqueDeviceId(),
      device_token = generateDeviceToken(),
      apple_id = apple_id,
      registration_time = LocalDateTime.now()
    )

    log.info(s"Created new user $user")
    registerUserToDB(user)
    return user
  }
}

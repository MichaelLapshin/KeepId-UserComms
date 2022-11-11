package common.client_database

/**
 * @file: DBSystemClientManager.scala
 * @description: Defines methods for interacting with the database, specialized in the KeepId's general functionality.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain
import com.typesafe.scalalogging.Logger

import java.sql.{PreparedStatement, ResultSet}

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBSystemClientManager {
  private val log = Logger(getClass.getName)

  def authenticateUser(user_id: Domain.UserId, connection: Boolean /* TODO, set valid param type */): Boolean = {
    log.info(s"Authenticating the user with ID ${user_id}.")
    // TODO: complete user authentication logic

    log.info(s"The user with ID ${user_id} has been authenticated.")
    true
  }

  def authenticateCompany(company_id: Domain.CompanyId, connection: Boolean /* TODO, set valid param type */): Boolean = {
    log.info(s"Authenticating the company with ID ${company_id}.")

    // TODO: complete company authentication logic
    log.info(s"The company with ID ${company_id} has been authenticated.")
    true
  }

  /**
   * existsUserId()
   * Check if the given user ID exists within the database.
   * @param user_id, the user ID to check for.
   * @return True, if the user ID has been found. False, otherwise.
   */
  def existsUserId(user_id: Domain.UserId): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM User WHERE user_id = ?")
    ps.setLong(1, user_id)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"User with ID ${user_id} does exists.")
      true
    } else {
      log.debug(s"User with ID ${user_id} does not exist.")
      false
    }
  }

  /**
   * existsUserPin()
   * Check if the given user PIN exists within the database.
   *
   * @param user_pin, the user PIN to check for.
   * @return True, if the user PIN has been found. False, otherwise.
   */
  def existsUserPin(user_pin: Domain.UserPin): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"User with PIN ${user_pin} does exists.")
      true
    } else {
      log.debug(s"User with PIN ${user_pin} does not exist.")
      false
    }
  }

  /**
   * existsDeviceId()
   * Checks if the given device ID exists within the database.
   * @param device_id, the device ID to check for.
   * @return True, if the device ID has been found. False, otherwise.
   */
  def existsDeviceId(device_id: Domain.DeviceId): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"Device with ID ${device_id} does exists.")
      true
    } else {
      log.debug(s"Device with ID ${device_id} does not exist.")
      false
    }
  }

  /**
   * getUserId()
   * Fetches the user ID mapped to the given device ID.
   *
   * @param device_id, The device ID to check against.
   * @return The user ID.
   */
  def getUserId(device_id: Domain.DeviceId): Domain.UserId = {
    log.debug(s"Getting the user ID of the user with device ID ${device_id}.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT user_id FROM User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      val user_id: Long = result.getLong("user_id")
      log.debug(s"Found that device ID ${device_id} maps to user ID ${user_id}.")
      return user_id
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        log.debug(s"Found no user ID that maps to the device ID ${device_id}.")
        throw new Throwable(f"Found multiple occurrences of '${device_id}' device ID in the database.")
      } else {
        log.warn(s"Found multiple user IDs that map to the device ID ${device_id}.")
        throw new Throwable(f"Did not find any occurrences of '${device_id}' device ID in the database.")
      }
    }
  }

  /**
   * getUserId()
   * Fetches the user ID mapped to the given user PIN.
   * @param user_pin, The user PIN to check against.
   * @return The user ID.
   */
  def getUserId(user_pin: Domain.UserPin): Domain.UserId = {
    log.debug(s"Getting the user ID of the user with PIN ${user_pin}.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT user_id FROM User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      val user_id: Long = result.getLong("user_id")
      log.debug(s"Found that user PIN ${user_pin} maps to user ID ${user_id}.")
      return user_id
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        log.debug(s"Found no user ID that maps to the user PIN ${user_pin}.")
        throw new Throwable(f"Found multiple occurrences of '${user_pin}' user PIN in the database.")
      } else {
        log.warn(s"Found multiple user IDs that map to the user PIN ${user_pin}.")
        throw new Throwable(f"Did not find any occurrences of '${user_pin}' user PIN in the database.")
      }
    }
  }
}

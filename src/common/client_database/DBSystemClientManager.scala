package common.client_database

/**
 * @file: DBSystemClientManager.scala
 * @description: Defines methods for interacting with the database, specialized in the KeepId's general functionality.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain

import java.sql.{PreparedStatement, ResultSet}

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBSystemClientManager {

  def authenticateUser(user_id: Domain.UserId, session: HttpSession): Boolean = {
    // TODO: complete this logic
  }

  def authenticateCompany(company_id: Domain.CompanyId, session: HttpSession): Boolean = {
    // TODO: complete this logic
  }

  /**
   * existsUserId()
   * Check if the given user ID exists within the database.
   * @param user_id, the user ID to check for.
   * @return True, if the user ID has been found. False, otherwise.
   */
  def existsUserId(user_id: Domain.UserId): Boolean = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT * FROM User WHERE user_id = ?")
    ps.setLong(1, user_id)
    val result: ResultSet = ps.executeQuery()
    return result.next()
  }

  /**
   * existsUserPin()
   * Check if the given user PIN exists within the database.
   *
   * @param user_pin, the user PIN to check for.
   * @return True, if the user PIN has been found. False, otherwise.
   */
  def existsUserPin(user_pin: Domain.UserPin): Boolean = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT * FROM User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()
    return result.next()
  }

  /**
   * existsDeviceId()
   * Checks if the given device ID exists within the database.
   * @param device_id, the device ID to check for.
   * @return True, if the device ID has been found. False, otherwise.
   */
  def existsDeviceId(device_id: Domain.DeviceId): Boolean = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT * FROM User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()
    return result.next()
  }

  /**
   * getUserId()
   * Fetches the user ID mapped to the given device ID.
   *
   * @param device_id, The device ID to check against.
   * @return The user ID.
   */
  def getUserId(device_id: Domain.DeviceId): Domain.UserId = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT user_id FROM User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      return result.getLong("user_id")
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        throw new Throwable(f"Found multiple occurrences of '${device_id}' device ID in the database.")
      } else {
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
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT user_id FROM User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      return result.getLong("user_id")
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        throw new Throwable(f"Found multiple occurrences of '${user_pin}' user PIN in the database.")
      } else {
        throw new Throwable(f"Did not find any occurrences of '${user_pin}' user PIN in the database.")
      }
    }
  }
}

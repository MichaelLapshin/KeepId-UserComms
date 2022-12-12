package common.client_database

/**
 * @file: DBSystemClientManager.scala
 * @description: Defines methods for interacting with the database, specialized in the KeepId's general functionality.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import akka.http.scaladsl.server.directives.Credentials
import common.constants.Domain
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import java.sql.{PreparedStatement, ResultSet}
import javax.net.ssl.SSLContext
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBSystemClientManager {
  private val log = Logger(getClass.getName)

  /**
   * existsUserId()
   * Check if the given user ID exists within the database.
   *
   * @param user_id , the user ID to check for.
   * @return True, if the user ID has been found. False, otherwise.
   */
  def existsUserId(user_id: Domain.UserId): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM ClientDatabase.User WHERE user_id = ?")
    ps.setLong(1, user_id)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"User with ID $user_id does exists.")
      true
    } else {
      log.debug(s"User with ID $user_id does not exist.")
      false
    }
  }

  /**
   * existsUserPin()
   * Check if the given user PIN exists within the database.
   *
   * @param user_pin , the user PIN to check for.
   * @return True, if the user PIN has been found. False, otherwise.
   */
  def existsUserPin(user_pin: Domain.UserPin): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM ClientDatabase.User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"User with PIN $user_pin does exists.")
      true
    } else {
      log.debug(s"User with PIN $user_pin does not exist.")
      false
    }
  }

  /**
   * existsDeviceId()
   * Checks if the given device ID exists within the database.
   *
   * @param device_id , the device ID to check for.
   * @return True, if the device ID has been found. False, otherwise.
   */
  def existsDeviceId(device_id: Domain.DeviceId): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM ClientDatabase.User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"Device with ID $device_id does exists.")
      true
    } else {
      log.debug(s"Device with ID $device_id does not exist.")
      false
    }
  }

  /**
   * getUserId()
   * Fetches the user ID mapped to the given device ID.
   *
   * @param device_id , The device ID to check against.
   * @return The user ID.
   */
  def getUserId(device_id: Domain.DeviceId): Domain.UserId = {
    log.debug(s"Getting the user ID of the user with device ID ${device_id}.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT user_id FROM ClientDatabase.User WHERE device_id = ?")
    ps.setLong(1, device_id)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      val user_id: Long = result.getLong("user_id")
      log.debug(s"Found that device ID $device_id maps to user ID $user_id.")
      return user_id
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        log.debug(s"Found no user ID that maps to the device ID $device_id.")
        throw new Throwable(f"Found multiple occurrences of '$device_id' device ID in the database.")
      } else {
        log.error(s"Found multiple user IDs that map to the device ID $device_id.")
        throw new Throwable(f"Did not find any occurrences of '$device_id' device ID in the database.")
      }
    }
  }

  /**
   * getUserId()
   * Fetches the user ID mapped to the given user PIN.
   *
   * @param user_pin , The user PIN to check against.
   * @return The user ID.
   */
  def getUserId(user_pin: Domain.UserPin): Domain.UserId = {
    log.debug(s"Getting the user ID of the user with PIN $user_pin.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT user_id FROM ClientDatabase.User WHERE user_pin = ?")
    ps.setNString(1, user_pin)
    val result: ResultSet = ps.executeQuery()

    // Extracts the user ID from the query result
    if (result.next()) {
      val user_id: Long = result.getLong("user_id")
      log.debug(s"Found that user PIN $user_pin maps to user ID $user_id.")
      return user_id
    } else {
      // If not one user ID was found to be mapped
      if (result.next()) {
        log.debug(s"Found no user ID that maps to the user PIN $user_pin.")
        throw new Throwable(f"Found multiple occurrences of '$user_pin' user PIN in the database.")
      } else {
        log.error(s"Found multiple user IDs that map to the user PIN $user_pin.")
        throw new Throwable(f"Did not find any occurrences of '$user_pin' user PIN in the database.")
      }
    }
  }

  def getDeviceId(user_id: Domain.UserId): Domain.DeviceId = {
    log.debug(f"Fetching the device ID of user with ID $user_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT device_id FROM ClientDatabase.User WHERE user_id = ?")
    ps.setLong(1, user_id)

    val result: ResultSet = ps.executeQuery()
    if (!result.next()) {
      log.warn(f"Could not find the device ID of user with ID $user_id.")
      throw new Throwable(f"Could not find the device ID of user with ID $user_id.")
    }
    val device_id = result.getLong("device_id")
    log.warn(f"Found that the user ID $user_id maps to device ID $device_id.")
    return device_id
  }

  def getCompanyId(company_host_id: Domain.CompanyHostId): Domain.CompanyId = {
    log.debug(f"Fetching the company ID of its host with ID $company_host_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT company_id FROM ClientDatabase.Company WHERE company_host_id = ?")
    ps.setLong(1, company_host_id)

    val result: ResultSet = ps.executeQuery()
    if (!result.next()) {
      log.warn(f"Could not find the company ID for the host ID $company_host_id.")
      throw new Throwable(f"Could not find the company ID for the host ID $company_host_id.")
    }
    val company_id = result.getLong("company_id")
    log.warn(f"Found that the company host ID $company_host_id maps to company ID $company_id.")
    return company_id
  }
}

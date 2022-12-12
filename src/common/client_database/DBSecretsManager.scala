package common.client_database

/**
 * @file: DBSecretsManager.scala
 * @description: Defines methods for interacting with the database, specialized in token and password fetching.
 * @author: KeepId Inc.
 * @date: December 7, 2022
 */

import common.constants.Domain
import com.typesafe.scalalogging.Logger

import java.sql.{PreparedStatement, ResultSet}

object DBSecretsManager {
  private val log = Logger(getClass.getName)

  def getDeviceToken(device_id: Domain.DeviceId): Domain.DeviceToken = {
    log.debug(f"Fetching the token of the device with ID $device_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT device_token FROM ClientDatabase.User WHERE device_id = ?")
    ps.setLong(1, device_id)

    val result: ResultSet = ps.executeQuery()
    if (!result.next()) {
      log.warn(f"Could not find user entry with device ID $device_id.")
      throw new Throwable(f"Could not find user entry with device ID $device_id.")
    }
    return result.getString("device_token") // do not log the token
  }

  def getCompanyPassword(company_id: Domain.CompanyId): Domain.CompanyPassword = {
    log.debug(f"Fetching the password of the company host with ID $company_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT company_password FROM ClientDatabase.Company WHERE company_id = ?")
    ps.setLong(1, company_id)

    val result: ResultSet = ps.executeQuery()
    if (!result.next()) {
      log.warn(f"Could not find password to the company with ID $company_id.")
      throw new Throwable(f"Could not find password to the company with ID $company_id.")
    }
    return result.getString("company_password") // do not log the password
  }

  def getCompanyHostToken(company_host_id: Domain.CompanyHostId): Domain.CompanyHostToken = {
    log.debug(f"Fetching the token of the company host with ID $company_host_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT company_host_token FROM ClientDatabase.Company WHERE company_host_id = ?")
    ps.setLong(1, company_host_id)

    val result: ResultSet = ps.executeQuery()
    if (!result.next()) {
      log.warn(f"Could not find company entry with company host ID $company_host_id.")
      throw new Throwable(f"Could not find company entry with company host ID $company_host_id.")
    }
    return result.getString("company_host_token") // do not log the token
  }
}

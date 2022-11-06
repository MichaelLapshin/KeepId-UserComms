package common.client_database

/**
 * @file: DBRequestManager.scala
 * @description: Defines methods for interacting with the database, specialized in the KeepId's request management.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.database_structs.Request
import common.constants.Domain

import java.sql.{PreparedStatement, ResultSet, Timestamp}
import java.time.LocalDateTime

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBRequestManager {

  /**
   * setRequestResponseTimeNow()
   * Sets the response time to the current datetime for the given request.
   * @param request_id, The request of which to set the time.
   * @return The number of rows that were updated.
   */
  private def setRequestResponseTimeNow(request_id: Domain.RequestId) = {
    // Updates the response time
    val ps: PreparedStatement = DatabaseIO.prepareStatement("UPDATE Request SET response_time = ? WHERE request_id = ?")
    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
    ps.setLong(2, request_id)

    val update_count = ps.executeUpdate()
    if (update_count == 0) {
      throw new Throwable(f"Could not find a request with ID ${request_id}")
    } else if (update_count > 1) {
      throw new Throwable(f"Found multiple requests with ID ${request_id}")
    }
  }

  /**
   * getRequestCompanyId()
   * Gets the ID of the company associated with the request.
   * @param request_id, the request to look for.
   * @return The ID of the company.
   */
  private def getRequestCompanyId(request_id: Domain.RequestId): Domain.CompanyId = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT company_id FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      throw new Throwable(f"Could not find the request ${request_id} within the client database.")
    }
    return result.getLong("company_id")
  }

  /**
   * registerRequestToDB()
   * Registers a new request to the client database.
   * @param request, The request to store in the database.
   */
  def registerRequestToDB(request: Request) = {
    val ps: PreparedStatement = DatabaseIO.prepareStatement("INSERT INTO ClientDatabase.Request (request_id, user_id, company_id, active_time) VALUES (?, ?, ?, ?)")
    ps.setLong(1, request.request_id)
    ps.setLong(2, request.user_id)
    ps.setLong(3, request.company_id)
    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()))
    ps.execute()

    // Records the data fields for the request
    for (df <- request.data_fields) {
      val df_ps: PreparedStatement = DatabaseIO.prepareStatement(
        "INSERT INTO ClientDatabase.RequestDataFields () VALUES (?, ?)"
      )
      df_ps.setLong(1, request.request_id)
      df_ps.setString(2, df)
      df_ps.execute()
    }
  }

  /**
   * getRequest()
   * Fetches the request from the client database.
   * @param request_id, the request to fetch.
   * @return A request object with data obtained from the client database.
   */
  def getRequest(request_id: Domain.RequestId): Request = {
    // Fetches the main information about the request
    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT request_id, user_id, company_id, active_time, response_time, expire_time FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    // Fetches the data fields associated with this request
    var df_list: List[Domain.DataField] = List()
    val df_ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT data_field FROM ClientDatabase.RequestDataFields WHERE request_id = ?")
    val df_result: ResultSet = df_ps.executeQuery()
    while (df_result.next()) {
      df_list += df_result.getString("data_field")
    }

    return new Request(
      request_id = result.getLong("request_id"),
      user_id = result.getLong("user_id"),
      company_id = result.getLong("company_id"),
      data_fields = df_list,
      active_time = result.getTimestamp("active_time").toLocalDateTime,
      response_time = result.getTimestamp("response_time").toLocalDateTime,
      expire_time = result.getTimestamp("expire_time").toLocalDateTime)
  }

  def rejectRequest(request_id: Domain.RequestId) = {

    if (setRequestResponseTimeNow(request_id) != 1) {

    }

    val ps: PreparedStatement = DatabaseIO.prepareStatement("INSERT ")
    ps.execute()
  }

  def reportRequest(request_id: Domain.RequestId,
                    report_message: Domain.ReportMessage) = {
    // TODO: complete this logic
  }

  def acceptRequest(request_id: Domain.RequestId,
                    encrypted_public_keys: Domain.EncryptedPublicKeys,
                    encrypted_private_keys: Domain.EncryptedPrivateKeys) = {
    // TODO: complete this logic
  }

  /**
   * getRequestDestinationUrl()
   * Fetches the URL destination for where the accepted request data should go.
   * @param request_id, the target request.
   * @return The URL of the system responsible for receiving user data requests for the company.
   */
  def getRequestDestinationUrl(request_id: Domain.RequestId): Domain.HostUrl = {
    val company_id: Domain.CompanyId = getRequestCompanyId(request_id)

    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT host_url FROM Database.Company WHERE company_id = ?")
    ps.setLong(1, company_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      throw new Throwable(f"Could nto find destination URL based on the request ID ${request_id}.")
    }
    return result.getString("host_url")
  }

  /**
   * getRequestDestinationCertificate()
   * Fetches the destination host certificate for where the accepted request data should go.
   *
   * @param request_id, the target request.
   * @return The certificate of the system responsible for receiving user data requests for the company.
   */
  def getRequestDestinationCertificate(request_id: Domain.RequestId): Domain.HostCertificate = {
    val company_id: Domain.CompanyId = getRequestCompanyId(request_id)

    val ps: PreparedStatement = DatabaseIO.prepareStatement("SELECT host_certificate FROM Database.Company WHERE company_id = ?")
    ps.setLong(1, company_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      throw new Throwable(f"Could nto find destination host certificate based on the request ID ${request_id}.")
    }
    return result.getString("host_certificate")
  }
}

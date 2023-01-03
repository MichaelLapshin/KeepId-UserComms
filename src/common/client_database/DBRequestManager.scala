package common.client_database

/**
 * @file: DBRequestManager.scala
 * @description: Defines methods for interacting with the database, specialized in the KeepId's request management.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.database_structs.{Request, CompanyHost}
import common.constants.Domain
import com.typesafe.scalalogging.Logger

import java.sql.{PreparedStatement, ResultSet, Timestamp}
import java.time.LocalDateTime

/**
 * NOTE: None of the operations below commit the transaction to the database.
 */
object DBRequestManager {
  private val log = Logger(getClass.getName)
  private val random = new scala.util.Random()
  private val randomRetryLimit = 1000

  /**
   * setRequestResponseTimeNow()
   * Sets the response time to the current datetime for the given request.
   * @param request_id, The request of which to set the time.
   * @return The number of rows that were updated.
   */
  private def setRequestResponseTimeNow(request_id: Domain.RequestId) = {
    log.debug(f"Setting the response time of request with ID $request_id to now.")

    // Updates the response time
    val ps: PreparedStatement = ClientDatabase.prepareStatement("UPDATE ClientDatabase.Request SET response_time = ? WHERE request_id = ?")
    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
    ps.setLong(2, request_id)

    val update_count = ps.executeUpdate()
    if (update_count == 0) {
      log.debug(f"Could not find a request with ID $request_id.")
      throw new Throwable(f"Could not find a request with ID $request_id.")
    } else if (update_count > 1) {
      log.error(f"Found multiple requests with ID $request_id.")
      throw new Throwable(f"Found multiple requests with ID $request_id.")
    }
    log.debug("Updated the request response time.")
  }

  /**
   * existsRequestId()
   * Check if the given request ID exists within the database.
   *
   * @param request_id , the request ID to check for.
   * @return True, if the request ID has been found. False, otherwise.
   */
  private def existsRequestId(request_id: Domain.RequestId): Boolean = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT * FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    if (result.next()) {
      log.debug(s"Request with ID $request_id does exists.")
      true
    } else {
      log.debug(s"Request with ID $request_id does not exist.")
      false
    }
  }
  /**
   * generateUniqueRequestId()
   * Generates a random unique request_id.
   *
   * @return Long integer representing the unique requesr_id.
   */
  def generateUniqueRequestId(): Domain.RequestId = {
    var attempts: Int = randomRetryLimit
    while (attempts > 0) {
      log.debug(s"Attempting to create a request user ID. Attempt: ${randomRetryLimit - attempts}")
      attempts -= 1

      // Generates a random user_id and validates its existence
      val request_id: Domain.RequestId = random.nextLong().abs
      if (existsRequestId(request_id) == false) {
        log.info(s"Found unique request ID ${request_id} after ${randomRetryLimit - attempts} attempts.")
        return request_id
      }
    }
    log.warn(f"Failed to generate a unique user_id within $randomRetryLimit attempts.")
    throw new Throwable(f"Failed to generate a unique user_id within $randomRetryLimit attempts.")
  }

  /**
   * getRequestUserId()
   * Gets the ID of the user associated with the request.
   *
   * @param request_id, the request to look for.
   * @return The ID of the user.
   */
  def getRequestUserId(request_id: Domain.RequestId): Domain.UserId = {
    log.debug(f"Getting user ID of the request with ID $request_id.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT user_id FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      log.debug(f"Could not find the request $request_id within the client database.")
      throw new Throwable(f"Could not find the request $request_id within the client database.")
    }
    val user_id: Long = result.getLong("user_id")
    log.debug(s"Found that the request with ID $request_id is mapped to the user ID $user_id.")
    return user_id
  }

  /**
   * getRequestCompanyId()
   * Gets the ID of the company associated with the request.
   * @param request_id, the request to look for.
   * @return The ID of the company.
   */
  private def getRequestCompanyId(request_id: Domain.RequestId): Domain.CompanyId = {
    log.debug(f"Getting company ID of the request with ID $request_id.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT company_id FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      log.debug(f"Could not find the request $request_id within the client database.")
      throw new Throwable(f"Could not find the request $request_id within the client database.")
    }
    val company_id: Long = result.getLong("company_id")
    log.debug(s"Found that the request with ID $request_id is mapped to the company ID $company_id.")
    return company_id
  }

  /**
   * registerRequestToDB()
   * Registers a new request to the client database.
   * @param request, The request to store in the database.
   */
  def registerRequestToDB(request: Request) = {
    log.debug(s"Registering the request ${request}.")

    val ps: PreparedStatement = ClientDatabase.prepareStatement("INSERT INTO ClientDatabase.Request (request_id, user_id, company_id, active_time) VALUES (?, ?, ?, ?)")
    ps.setLong(1, request.request_id)
    ps.setLong(2, request.user_id)
    ps.setLong(3, request.company_id)
    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()))
    ps.execute()

    // Records the data fields for the request
    for (df <- request.data_fields) {
      log.debug(s"For request with ID ${request.request_id}, registering the data field '${df}'.")
      val df_ps: PreparedStatement = ClientDatabase.prepareStatement(
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
    log.debug(s"Getting the request with ID ${request_id}.")

    // Fetches the main information about the request
    val ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT request_id, user_id, company_id, active_time, response_time, expire_time FROM ClientDatabase.Request WHERE request_id = ?")
    ps.setLong(1, request_id)
    val result: ResultSet = ps.executeQuery()

    // Fetches the data fields associated with this request
    var df_list: List[Domain.DataField] = List()
    val df_ps: PreparedStatement = ClientDatabase.prepareStatement("SELECT data_field FROM ClientDatabase.RequestDataFields WHERE request_id = ?")
    val df_result: ResultSet = df_ps.executeQuery()
    while (df_result.next()) {
      df_list ::= df_result.getString("data_field")
    }

    val request = new Request(
      request_id = result.getLong("request_id"),
      user_id = result.getLong("user_id"),
      company_id = result.getLong("company_id"),
      data_fields = df_list,
      active_time = result.getTimestamp("active_time").toLocalDateTime,
      response_time = result.getTimestamp("response_time").toLocalDateTime,
      expire_time = result.getTimestamp("expire_time").toLocalDateTime)

    log.debug(f"Found the request with ID ${request_id} to store ${request}.")
    return request
  }

  def rejectRequest(request_id: Domain.RequestId): Unit = {
    log.debug(s"Rejecting the request with ID ${request_id}.")
    setRequestResponseTimeNow(request_id)
  }

  def reportRequest(request_id: Domain.RequestId,
                    report_message: Domain.ReportMessage): Unit = {
    log.debug(s"Reporting the request with ID ${request_id} with the message '${report_message}'.")

    setRequestResponseTimeNow(request_id)
    val ps: PreparedStatement = ClientDatabase.prepareStatement("INSERT INTO ClientDatabase.RequestReport (request_id, report_message) VALUES (?, ?)")
    ps.setLong(1, request_id)
    ps.setString(2, report_message)
    ps.execute()
  }

  def acceptRequest(request_id: Domain.RequestId): Unit = {
    log.debug(s"Accepting the request with ID ${request_id}.")

    setRequestResponseTimeNow(request_id)
    val ps: PreparedStatement = ClientDatabase.prepareStatement("INSERT INTO ClientDatabase.RequestAccept (request_id) VALUES (?)")
    ps.setLong(1, request_id)
    ps.execute()
  }

  /**
   * getCompanyHostInfo()
   * Fetches the URL destination for where the accepted request data should go.
   * @param request_id, the target request.
   * @return The URL of the system responsible for receiving user data requests for the company.
   */
  def getCompanyHostInfo(request_id: Domain.RequestId): CompanyHost = {
    log.debug(s"Getting the company host information of the request with ID ${request_id}.")

    val company_id: Domain.CompanyId = getRequestCompanyId(request_id)

    val ps: PreparedStatement = ClientDatabase.prepareStatement(
      "SELECT company_host_id, company_host_url, company_host_token FROM Database.Company WHERE company_id = ?"
    )
    ps.setLong(1, company_id)
    val result: ResultSet = ps.executeQuery()

    if (!result.next()) {
      log.warn(f"Could not find company host information based on the request ID $request_id.")
      throw new Throwable(f"Could nto find company host information based on the request ID $request_id.")
    }

    val company_host = new CompanyHost(
      company_host_id = result.getLong("company_host_id"),
      company_host_url = result.getString("company_host_url"),
      company_host_token = result.getString("company_host_token")
    )
    log.debug(s"Found the company host information $company_host for request with ID $request_id.")
    return company_host
  }

}

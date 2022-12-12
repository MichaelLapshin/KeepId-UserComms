package common.client_database

/**
 * @file: DBRequestCollections.scala
 * @description: Defines methods for fetching collections of requests the Client Database.
 * @author: KeepId Inc.
 * @date: December 8, 2022
 */

import common.database_structs.Request
import common.constants.Domain
import com.typesafe.scalalogging.Logger

import java.sql.{PreparedStatement, ResultSet, Timestamp}
import java.time.{Duration, LocalDateTime}

object DBRequestCollections {
  private val log = Logger(getClass.getName)

  /**
   * getRequests()
   * Get the list of requests given a prepared statement which queries for request IDs.
   *
   * @param ps , the prepared statement to query.
   * @return list of requests.
   */
  def getRequests(ps: PreparedStatement): List[Request] = {
    // Fetch the list of request IDs to query
    val result: ResultSet = ps.executeQuery()
    var request_id_list = List[Domain.RequestId]()
    while (result.next()) { request_id_list :+ result.getLong("request_id") }

    // Get the requests
    var request_list = List[Request]()
    for (id <- request_id_list) {
      request_list :+ DBRequestManager.getRequest(id)
    }
    return request_list
  }

  /**
   * getUserInactiveRequests()
   * Get the list of inactive requests for a user within a set time-frame.
   * @param user_id, the user to find the related requests from.
   * @param valid_time_min, the minimum valid time to search for.
   * @param valid_time_max, the maximum valid time to search for.
   * @return list of inactive requests.
   */
  def getUserInactiveRequests(user_id: Domain.UserId, valid_time_min: LocalDateTime,
                              valid_time_max: LocalDateTime = LocalDateTime.now()): List[Request] = {
    log.debug(f"Fetching inactive requests from active time $valid_time_min to $valid_time_max for user with ID $user_id.")
    val ps: PreparedStatement = ClientDatabase.prepareStatement(
      "SELECT request_id FROM ClientDatabase.Request WHERE " +
        "(expire_time < ? OR response_time IS NOT NULL) " +
        "AND user_id = ? AND ? <= valid_from && valid_from <= ? " +
        "ORDER BY active_time DESC"
    )
    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
    ps.setLong(2, user_id)
    ps.setTimestamp(3, Timestamp.valueOf(valid_time_min))
    ps.setTimestamp(4, Timestamp.valueOf(valid_time_max))
    return getRequests(ps)
  }

  /**
   * getUserActiveRequests()
   * Get the list of all active requests for a user.
   * @param user_id, the user for which to find the active requests.
   * @return list of all active requests.
   */
  def getUserActiveRequests(user_id: Domain.UserId): List[Request] = {
    val ps: PreparedStatement = ClientDatabase.prepareStatement(
      "SELECT request_id FROM ClientDatabase.Request WHERE " +
        "? <= expire_time AND response_time IS NULL AND user_id = ? " +
        "ORDER BY active_time DESC"
    )
    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
    ps.setLong(2, user_id)
    return getRequests(ps)
  }

}

package common.client_database

/**
 * @file: DatabaseIO.scala
 * @description: Object for handling interactions with the KeepId client database.
 * @author: KeepId Inc.
 * @date: November 1, 2022
 */

import java.sql.{Connection, DriverManager}

/**
 * DatabaseIO
 * Class for abstracting the database interface.
 *
 * Note: No statement commits automatically.
 */
object DatabaseIO {
  private val driver = "com.mysql.jdbc.Driver"
  var connection: Connection = _
  openConnection()

  /**
   * openConnection()
   * Attempts to connect to the database defined by the following environment variables:
   *     CLIENT_DB_URL: The URL used to access the database.
   *     CLIENT_DB_USERNAME: The username of the client database.
   *     CLIENT_DB_PASSWORD: The password of the database.
   */
  def openConnection(): Unit = {
    Class.forName(driver)

    // Connects to the database
    val url: String = sys.env("CLIENT_DB_URL")
    val username: String = sys.env("CLIENT_DB_USERNAME")
    val password: String = sys.env("CLIENT_DB_PASSWORD")
    connection = DriverManager.getConnection(url, username, password)

    // Set connection configurations
    connection.setAutoCommit(false)
  }

  /**
   * commit()
   * Commits the changes done as part of a query for the client database.
   */
  def commit() = connection.commit()

  /**
   * rollback()
   * Rolls-back any changes made until the previous commit.
   */
  def rollback() = connection.rollback()

  /**
   * closeConnection()
   * Closes the connection to the client database.
   */
  def closeConnection() = connection.close()
}

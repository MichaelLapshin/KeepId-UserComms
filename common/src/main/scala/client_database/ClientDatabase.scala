package keepid.common.client_database

/**
 * @file: ClientDatabase.scala
 * @description: Object for handling interactions with the KeepId client database.
 * @author: KeepId Inc.
 * @date: November 1, 2022
 */

import com.typesafe.scalalogging.Logger

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

/**
 * ClientDatabase
 * Class for abstracting the database interface.
 *
 * Note: No statement commits automatically.
 */
object ClientDatabase {
  private val log = Logger(getClass.getName)
  private val driver = "com.mysql.cj.jdbc.Driver"
  private val schema = "ClientDatabase"
  private var connection: Connection = _

  /**
   * openConnection()
   * Attempts to connect to the database defined by the following environment variables:
   *     CLIENT_DB_URL: The URL used to access the database.
   *     CLIENT_DB_USERNAME: The username of the client database.
   *     CLIENT_DB_PASSWORD: The password of the database.
   */
  def openConnection(read_only: Boolean = false): Unit = {
    log.info("Opening the connection to the client database.")
    Class.forName(driver)

    // Connects to the database // TODO, remove hard-coding
    val url: String = "jdbc:mysql://keepid-client-database.mysql.database.azure.com:3306/ClientDatabase" //?useSSL=true" // sys.env("CLIENT_DB_URL")
    val username: String = "dbadmin" // sys.env("CLIENT_DB_USERNAME")
    val password: String = "A7BgmvNa42UAdHQ" // sys.env("CLIENT_DB_PASSWORD")
    connection = DriverManager.getConnection(url, username, password)

    // Set connection configurations
    connection.setAutoCommit(false)
    connection.setSchema(schema)
    connection.setReadOnly(read_only)
    connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT)
    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE)

    log.info("Connection to the client database is now open.")
  }

  /**
   * prepareStatement()
   * @param stmt, the statement to prepare.
   * @return PreparedStatement
   */
  def prepareStatement(stmt: String): PreparedStatement = {
    log.debug(f"Preparing the statement: ${stmt}")
    connection.prepareStatement(stmt)
  }

  /**
   * commit()
   * Commits the changes done as part of a query for the client database.
   */
  def commit() = {
    log.debug("Committing database changes.")
    connection.commit()
  }

  /**
   * rollback()
   * Rolls-back any changes made until the previous commit.
   */
  def rollback() = {
    log.debug("Rolling back database changes.")
    connection.rollback()
  }

  /**
   * closeConnection()
   * Closes the connection to the client database.
   */
  def closeConnection() = {
    log.info("Closing the connection to the client database.")
    connection.close()
  }
}

package common.database_logic.database_io

/**
 * @file: DatabaseIO_MySQL.scala
 * @description: Object for handling interactions with a MySQL database.
 * @author: KeepId Inc.
 * @date: November 1, 2022
 */

import common.database_logic.database_io.DatabaseIO.ValType
import common.database_logic.database_io.DatabaseIO_Interface

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import scala.collection.immutable.Seq

class DatabaseIO_MySQL extends DatabaseIO_Interface {
  val url = "jdbc:mysql://localhost:8889/mysql"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "root"
  var connection: Connection = _

  // Connect to the database
  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)

    // Set connection configurations
    connection.setAutoCommit(false)
  } catch {
    case e: Exception => e.printStackTrace
  }

  def preparedStatement(statement: String): PreparedStatement = connection.prepareStatement(statement)

  // Validate that the necessary tables are present in the database
  def executeQuery[T](query: String, values: Seq[ValType]): Iterator[T] = {
    val ps: PreparedStatement = connection.prepareStatement()
    val statement = connection.prepareStatement("SELECT host, user FROM user")
    val rs: ResultSet = statement.executeQuery()
    while (rs.next) {
      val host = rs.getString("host")
      val user = rs.getString("user")
      println(f"host = ${host}, user = ${user}")
    }

    connection.commit()
  }

  /**
   * Interface method requirements.
   */
  def executeQuery[T](query: String, values: Seq[ValType]): Iterator[Map[String, Object]] = {
    // Prepare the query
    val pStmt: PreparedStatement = connection.prepareStatement(query)
    var index: Int = 1
    for (v <- values) {
      if (v.l.isDefined) pStmt.setLong(index, v.l.get)
      else if (v.s.isDefined) pStmt.setString(index, v.s.get)
      else if (v.t.isDefined) pStmt.setTime(index, v.t.get)
      index += 1
    }

    // Obtain result
    val rs: ResultSet = pStmt.executeQuery()
    Iterator
      .continually(rs.next)
      .takeWhile(identity )
      .map(_ => rs.getString(1) -> rs.getString(2))
      .toMap
  }

  def commit() = connection.commit()

  def rollback() = connection.rollback()

  def closeConnection() = connection.close()
}
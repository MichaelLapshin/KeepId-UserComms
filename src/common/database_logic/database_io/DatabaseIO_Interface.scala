package common.database_logic.database_io

import common.database_logic.database_io.DatabaseIO.ValType

/**
 * @file: DatabaseIO_MySQL.scala
 * @description: Object for handling interactions with a MySQL database.
 * @author: KeepId Inc.
 * @date: November 1, 2022
 */

trait DatabaseIO_Interface {
  def execute(stmt: String, values: Seq[ValType]): Boolean
  def executeUpdate(update: String, values: Seq[ValType]): Int
  def executeQuery[T](query: String, values: Seq[ValType]): Iterator[T]
  def commit()
  def rollback()
  def closeConnection()
}
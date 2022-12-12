package services.encrypted_data_sender

import com.typesafe.scalalogging.Logger

/**
 * @file: EncryptedDataSenderServer.scala
 * @description: The object in charge of starting and stopping the Encrypted Data Sender Server.
 * @author: KeepId Inc.
 * @date: October 1, 2022
 */

object EncryptedDataSenderServer {
  private val log = Logger(getClass.getName)

  def main(args: Array[String]) = {
    log.info("Starting the Encrypted Data Sender Server...")
    EncryptedDataSender.runLogic()
    log.info("The server has been stopped.")
  }
}

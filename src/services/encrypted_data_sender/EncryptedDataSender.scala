package services.encrypted_data_sender

/**
 * @file: EncryptedDataSender.scala
 * @description: Define the class used to fetch and forward the encrypted user data to a company.
 * @author: KeepId Inc.
 * @date: October 1, 2022
 */

import common.constants.Domain
import org.slf4j.LoggerFactory

class EncryptedDataSender extends Thread {
  private val log = LoggerFactory.getLogger(this.getClass)

  private def forwardEncryptedData(url: Domain.HostUrl, certificate: Domain.HostCertificate): Unit = {
    // TODO: complete the logic here.
  }

  override def run(): Unit = {
    // TODO: complete the logic here.
  }
}

package service.encrypted_data_sender

/**
 * @file: EncryptedDataSender.scala
 * @description: Define the class used to fetch and forward the encrypted user data to a company.
 * @author: KeepId
 * @date: October 1, 2022
 */

import common.{Connection, Domain, Producer}
import service.encrypted_data_sender.EncryptedDataSenderJsonProtocol.{
  encryptedDataReceiveFormat,
  encryptedDataForwardFormat
}

class EncryptedDataSender extends Thread {
  private def forwardEncryptedData(address: Domain.HostAddress, certificate: Domain.HostCertificate): Unit = {
    // TODO: complete the logic here.
  }

  override def run(): Unit = {
    // TODO: complete the logic here.
  }
}
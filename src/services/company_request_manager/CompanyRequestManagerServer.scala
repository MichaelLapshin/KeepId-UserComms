package services.company_request_manager

/**
 * @file: CompanyRequestManagerServer.scala
 * @description: The service in charge of the business logic and
 *               delivery of the data request private keys.
 * @author: KeepId Inc.
 * @date: December 12, 2022
 */

import com.typesafe.scalalogging.Logger
import common.message_broker.{ForeverConsumer, Topics}

object CompanyRequestManagerServer {
  private val log = Logger(getClass.getName)
  private val consumer = new ForeverConsumer(getClass.getName)

  def main(args: Array[String]) = {
    log.info("Starting the Company Request Manager Server...")

    consumer.subscribe(Topics.EncryptedDataPrivateKeyTopic)
    consumer.pollAndProcessForever(record =>
      try {

      } catch {
        match _ => log.error("Caught an error while poll")
      }
    )

    log.info("The server has been stopped.")
  }
}

package services.company_request_manager

/**
 * @file: CompanyRequestManagerServer.scala
 * @description: The service in charge of the business logic and
 *               delivery of the data request private keys.
 * @author: KeepId Inc.
 * @date: December 12, 2022
 */

import com.typesafe.scalalogging.Logger

object CompanyRequestManagerServer {
  private val log = Logger(getClass.getName)

  def main(args: Array[String]) = {
    log.info("Starting the Company Request Manager Server...")

    CompanyRequestManager.runLogic()

    log.info("The server has been stopped.")
  }
}

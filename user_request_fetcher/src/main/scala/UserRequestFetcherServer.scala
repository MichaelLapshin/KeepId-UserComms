package keepid.services.user_request_fetcher

/**
 * @file: UserRequestFetcherServer.scala
 * @description: The object in charge of starting and stopping the User Request Fetch Server.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import com.typesafe.scalalogging.Logger

object UserRequestFetcherServer {
  private val log = Logger(getClass.getName)

  def main(args: Array[String]) = {
    log.info("Starting the User Request Fetch Server...")

    // TODO: complete the logic here.

    log.info("The server has been stopped.")
  }
}

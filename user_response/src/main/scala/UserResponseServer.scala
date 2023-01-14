package keepid.services.user_response

/**
 * @file: UserResponseServer.scala
 * @description: The object in charge of starting and stopping the User Response Server.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import com.typesafe.scalalogging.Logger

object UserResponseServer {
  private val log = Logger(getClass.getName)

  def main(args: Array[String]) = {
    log.info("Starting the User Response Server...")

    // TODO: complete the logic here.

    log.info("The server has been stopped.")
  }
}

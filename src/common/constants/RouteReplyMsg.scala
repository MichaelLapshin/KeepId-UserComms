package common.constants

/**
 * @file: RouteReplyMsg.scala
 * @description: Define the different message replies used when returning information to the HTTPS connection initiator.
 * @author: KeepId Inc.
 * @date: October 28, 2022
 */

object RouteReplyMsg {
  val Ping: String = "I'm alive."
  val InvalidRoute: String = "Invalid route."
}
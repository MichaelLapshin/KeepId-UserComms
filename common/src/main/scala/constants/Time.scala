package keepid.common.constants

/**
 * @file: Time.scala
 * @description: Define the formatting standards and functions related to time.
 * @author: KeepId Inc.
 * @date: October 14, 2022
 */

import java.time.format.DateTimeFormatter

object Time {
  val StringFormat = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:MM:SS.SSS")
  val NullTime = null
}

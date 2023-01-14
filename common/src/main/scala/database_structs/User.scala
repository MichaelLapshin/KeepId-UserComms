package keepid.common.database_structs

/**
 * @file: User.scala
 * @description: Define the structure for holding user client database information.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

import keepid.common.constants.Domain

import java.time.LocalDateTime

class User(val user_id: Domain.UserId,
           val user_pin: Domain.UserPin,
           val device_id: Domain.DeviceId,
           val device_token: Domain.DeviceToken,
           val apple_id: Domain.AppleId,
           val registration_time: LocalDateTime) {
  override def toString: String =
    f"{ user_id:$user_id, user_pin:$user_pin, device_id:$device_id, device_token:[secret_token], " +
      f"apple_id:$apple_id, registration_time:$registration_time}"
}

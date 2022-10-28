package common.database_structs

/**
 * @file: UserIdMap.scala
 * @description: Define the structure for holding user client identification information.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

import common.constants.Domain

class UserIdMap(val user_id: Domain.UserId,
                val user_pin: Domain.UserPin,
                val device_id: Domain.DeviceId)

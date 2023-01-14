package keepid.services.user_request_manager

import com.typesafe.scalalogging.Logger
import keepid.common.constants.Domain

abstract class PushNotification {
  private val log = Logger(getClass.getName)

  def sendRequestNotification(user_id: Domain.UserId): Unit
}
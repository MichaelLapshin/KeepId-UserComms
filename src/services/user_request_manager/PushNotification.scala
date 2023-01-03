package services.user_request_manager

import common.constants.Domain
import com.typesafe.scalalogging.Logger

abstract class PushNotification {
  private val log = Logger(getClass.getName)

  def sendRequestNotification(user_id: Domain.UserId): Unit
}
package services.user_request_manager

import services.user_request_manager.PushNotification
import common.constants.Domain
import com.typesafe.scalalogging.Logger
import common.constants.Domain

class PushNotification_Apple extends PushNotification {
  private val log = Logger(getClass.getName)

  override def sendRequestNotification(user_id: Domain.UserId): Unit = {

  }
}
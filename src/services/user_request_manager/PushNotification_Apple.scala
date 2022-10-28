package services.user_request_manager

import services.user_request_manager.PushNotification
import common.Domain

object PushNotification_Apple extends PushNotification {
  override def sendRequestNotification(user_id: Domain.UserId): Unit = {

  }
}
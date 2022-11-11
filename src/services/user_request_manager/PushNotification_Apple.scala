package services.user_request_manager

import services.user_request_manager.PushNotification
import common.Domain
import com.typesafe.scalalogging.Logger

object PushNotification_Apple extends PushNotification {
  private val log = Logger(getClass.getName)

  override def sendRequestNotification(user_id: Domain.UserId): Unit = {

  }
}
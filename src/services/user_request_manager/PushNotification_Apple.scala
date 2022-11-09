package services.user_request_manager

import services.user_request_manager.PushNotification
import common.Domain
import org.slf4j.LoggerFactory

object PushNotification_Apple extends PushNotification {
  private val log = LoggerFactory.getLogger(this.getClass)

  override def sendRequestNotification(user_id: Domain.UserId): Unit = {

  }
}
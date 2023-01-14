package keepid.services.user_request_manager

import keepid.services.user_request_manager.PushNotification
import com.typesafe.scalalogging.Logger
import keepid.common.constants.Domain

class PushNotification_Apple extends PushNotification {
  private val log = Logger(getClass.getName)

  override def sendRequestNotification(user_id: Domain.UserId): Unit = {

  }
}
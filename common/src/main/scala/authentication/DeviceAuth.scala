package keepid.common.authentication

/**
 * @file: DeviceAuth.scala
 * @description: Define the authentication method for user devices.
 * @author: KeepId Inc.
 * @date: December 3, 2022
 */

import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.Logger
import keepid.common.client_database.{DBSecretsManager, DBSystemClientManager}
import keepid.common.constants.Domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DeviceAuth {
  private val log = Logger(getClass.getName)
  val realm: String = "SecureDeviceSite"

  /**
   * authenticate()
   * Authenticates the user device given:
   *  - username <- device_id
   *  - password <- device_token
   *
   * @param credentials, the credentials containing the device ID and token.
   * @return The user ID associated with the authenticated device. None if authentication fails.
   */
  def authenticate(credentials: Credentials): Future[Option[Domain.UserId]] = {
    try {
      credentials match {
        case device_token@Credentials.Provided(device_id) =>
          Future {
            log.info(s"Authenticating the user with device ID $device_id.")
            val db_user_id: Domain.UserId = DBSystemClientManager.getUserId(device_id)
            val db_device_token: Domain.DeviceToken = DBSecretsManager.getDeviceToken(device_id.toLong)
            if (device_token.verify(db_device_token)) { // Must use verify to avoid the "Credentials timing attack"
              log.info(s"The device of user with ID $db_user_id has been authenticated.")
              Some(db_user_id)
            }
            else None
          }
        case _ => Future.successful(None)
      }
    } catch {
      case x: Throwable =>
        log.warn(s"Exception occurred while authenticating a user. Error: ${x}")
        Future.successful(None)
    }
  }



}

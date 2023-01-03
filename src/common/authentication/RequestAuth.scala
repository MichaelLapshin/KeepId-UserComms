package common.authentication

/**
 * @file: DeviceAuth.scala
 * @description: Define the authentication method for data requests.
 * @author: KeepId Inc.
 * @date: December 3, 2022
 */

import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.Logger
import common.client_database.{DBRequestManager, DBSecretsManager, DBSystemClientManager}
import common.constants.Domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object RequestAuth {
  private val log = Logger(getClass.getName)
  val realm: String = "SecureRequestSite"

  /**
   * authenticate()
   * Authenticates the request for a device given:
   *  - username <- request_id
   *  - password <- device_token
   *
   * @param credentials, the credentials containing the request ID and device token.
   * @return The request ID. None if authentication fails.
   */
  def authenticate(credentials: Credentials): Future[Option[Domain.RequestId]] = {
    try {
      credentials match {
        case device_token@Credentials.Provided(request_id) =>
          Future {
            log.info(s"Authenticating the request with ID $request_id.")
            val db_user_id: Domain.UserId = DBRequestManager.getRequestUserId(request_id.toLong)
            val db_device_id: Domain.DeviceId = DBSystemClientManager.getDeviceId(db_user_id)
            val db_device_token: Domain.DeviceToken = DBSecretsManager.getDeviceToken(db_device_id)
            if (device_token.verify(db_device_token)) { // Must use verify to avoid the "Credentials timing attack"
              log.info(s"The user with ID $db_user_id has been authenticated.")
              Some(request_id.toLong)
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

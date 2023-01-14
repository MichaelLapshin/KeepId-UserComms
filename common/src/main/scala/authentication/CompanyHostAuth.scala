package keepid.common.authentication

/**
 * @file: CompanyHostAuth.scala
 * @description: Define the authentication method for company hosts.
 * @author: KeepId Inc.
 * @date: December 5, 2022
 */

import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.Logger
import keepid.common.client_database.{DBSecretsManager, DBSystemClientManager}
import keepid.common.constants.Domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CompanyHostAuth {
  private val log = Logger(getClass.getName)
  val realm: String = "SecureCompanyHostSite"

  /**
   * authenticate()
   * Authenticates the company host given:
   *  - username <- company_host_id
   *  - password <- company_host_token
   *
   * @param credentials, the credentials containing the company host ID and token.
   * @return The company ID associated with the company host. None if authentication fails.
   */
  def authenticate(credentials: Credentials): Future[Option[Domain.CompanyId]] = {
    try {
      credentials match {
        case company_host_token@Credentials.Provided(company_host_id) =>
          Future {
            log.info(s"Authenticating the company host with ID $company_host_id.")
            val db_company_id: Domain.CompanyId = DBSystemClientManager.getCompanyId(company_host_id.toLong)
            val db_company_host_token: Domain.CompanyHostToken = DBSecretsManager.getCompanyHostToken(company_host_id.toLong)
            if (company_host_token.verify(db_company_host_token)) { // Must use verify to avoid the "Credentials timing attack"
              log.info(s"The company host of company with ID $db_company_id has been authenticated.")
              Some(db_company_id)
            }
            else None
          }
        case _ => Future.successful(None)
      }
    } catch {
      case x: Throwable =>
        log.warn(s"Exception occurred while authenticating a company. Error: ${x}")
        Future.successful(None)
    }
  }
}

package common.authentication

/**
 * @file: CompanyAuth.scala
 * @description: Define the authentication method for companies.
 * @author: KeepId Inc.
 * @date: December 5, 2022
 */

import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.Logger
import common.client_database.DBSecretsManager
import common.constants.Domain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CompanyAuth {
  private val log = Logger(getClass.getName)
  val realm: String = "SecureCompanySite"

  /**
   * authenticate()
   * Authenticates the company given:
   *  - username <- company_pin
   *  - password <- company_token
   *
   * @param credentials, the credentials containing the company PIN and token.
   * @return The company ID. None if authentication fails.
   */
  def authenticate(credentials: Credentials): Future[Option[Domain.CompanyId]] = {
    try {
      credentials match {
        case company_password@Credentials.Provided(company_id) =>
          Future {
            log.info(s"Authenticating the company with ID $company_id.")
            val db_company_password: Domain.CompanyPassword = DBSecretsManager.getCompanyPassword(company_id.toLong)
            if (company_password.verify(db_company_password)) { // Must use verify to avoid the "Credentials timing attack"
              log.info(s"The company host with ID $company_id has been authenticated.")
              Some(company_id.toLong)
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

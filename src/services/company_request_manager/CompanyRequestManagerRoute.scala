package services.company_request_manager

/**
 * @file: CompanyRequestManagerRoute.scala
 * @description: Defines the API and business logic between company hosts and the Keep ecosystem.
 * @author: KeepId Inc.
 * @date: September 29, 2022
 */

import spray.json._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpProtocols, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.Logger
import common.authentication.CompanyHostAuth
import common.client_database.ClientDatabase
import common.constants.HttpPaths

class CompanyRequestManagerRoute extends Directives with CompanyRequestManagerJsonProtocol {
  private val log = Logger(getClass.getName)

  // Route definition
  lazy val requestManagerRoute: Route = concat(
    path(HttpPaths.CompanyRequestManager.AcceptDataRequest) {
      authenticateBasicAsync(realm = CompanyHostAuth.realm, CompanyHostAuth.authenticate) { company_id =>
        post {
          entity(as[CompanyRequestManagerHostReceiveData]) { data =>
            try {
              val return_data = CompanyRequestManagerHostReturnData(
                request_id = data.request_id,
                private_key = keyStore
              )

              ClientDatabase.commit()
              complete(
                HttpResponse(
                  status = StatusCodes.OK,
                  entity = HttpEntity(ContentTypes.`application/json`, return_data.toJson.compactPrint),
                  protocol = HttpProtocols.`HTTP/2.0`
                )
              )
            } catch {
              case _: Throwable =>
                log.warn(s"Exception occurred while trying to server http request with error: ${_}")
                ClientDatabase.rollback()
                complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
    },
    path(HttpPaths.Ping) {
      get {
        log.info("Received ping request.")
        complete(StatusCodes.OK)
      }
    }
  )


}

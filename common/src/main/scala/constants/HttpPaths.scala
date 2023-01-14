package keepid.common.constants

/**
 * @file: HttpPaths.scala
 * @description: Defines the paths used to communicate with the services.
 * @author: KeepId Inc.
 * @date: December 9, 2022
 */

// Warning: Make sure that the routes are unique
object HttpPaths {
  val Ping: String = "ping"

  /** Define the route paths used to perform the request update logic. */
  object UserUpdate {
    val UpdateData: String = "update"
  }

  /** Define the route paths used to perform the request response logic. */
  object UserResponse {
    val DataRequest: String = "data_request"
    val AcceptDataRequest: String = DataRequest + "/accept"
    val RejectDataRequest: String = DataRequest + "/reject"
    val ReportDataRequest: String = DataRequest + "/report"
  }

  /** Define the route paths used to perform the request management logic. */
  object UserRequestManager {
    val InitiateDataRequest: String = "data_request/initiate"
  }

  /** Define the route paths used to perform the user request fetcher logic. */
  object UserRequestFetcher {
    val DataRequestsGet: String = "data_requests/get"
    val GetRequestsActive: String = DataRequestsGet + "/active"
    val GetRequestsExpired: String = DataRequestsGet + "/expired"
    val GetRequestsRejected: String = DataRequestsGet + "/rejected"
    val GetRequestsReported: String = DataRequestsGet + "/reported"
  }

  /** Define the route paths used to perform the user request fetcher logic. */
  object UserIdManager {
    val UserCreate: String = "create"
  }

  /** Define the route paths used to perform the company request manager logic. */
  object CompanyRequestManager {
    val AcceptDataRequest: String = "data_request/accept"
  }
}

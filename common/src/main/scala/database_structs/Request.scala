package keepid.common.database_structs

/**
 * @file: Request.scala
 * @description: Define the structure for holding request information.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

import keepid.common.constants.Domain

import java.time.LocalDateTime

class Request(val request_id: Domain.RequestId,
              val user_id: Domain.UserId,
              val company_id: Domain.CompanyId,
              val data_fields: Domain.ExpectedDataFields,
              val active_time: LocalDateTime,
              val response_time: LocalDateTime,
              val expire_time: LocalDateTime) {
  override def toString: String =
    f"{ request_id:'$request_id', user_id:'$user_id', company_id:'$company_id', data_fields:'$data_fields'," +
      f" active_time:'$active_time', response_time:'$response_time', expire_time:'$expire_time' }"
}

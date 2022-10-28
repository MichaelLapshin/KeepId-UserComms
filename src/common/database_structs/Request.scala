package common.database_structs

/**
 * @file: Request.scala
 * @description: Define the structure for holding request information.
 * @author: KeepId Inc.
 * @date: October 13, 2022
 */

import common.constants.Domain
import java.time._

class Request(val request_id: Domain.RequestId,
              val company_id: Domain.CompanyId,
              val data_fields: Domain.ExpectedDataFields,
              val active_time: LocalDateTime,
              val response_time: LocalDateTime,
              val expire_time: LocalDateTime)

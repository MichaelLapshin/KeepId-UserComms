package keepid.common.database_structs

/**
 * @file: CompanyHost.scala
 * @description: Define the structure for holding company host information.
 * @author: KeepId Inc.
 * @date: December 12, 2022
 */

import keepid.common.constants.Domain

class CompanyHost(val company_host_id: Domain.CompanyHostId,
                  val company_host_url: Domain.CompanyHostUrl,
                  val company_host_token: Domain.CompanyHostToken) {
  override def toString: String =
    f"{ company_host_id:$company_host_id, company_host_url:$company_host_url, company_host_token:[secret_token] }"
}

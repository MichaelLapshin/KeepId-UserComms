package keepid.services.company_request_manager

/**
 * @file: PrivateKeyStore.scala
 * @description: Stores the private keys of request data.
 * @author: KeepId Inc.
 * @date: December 12, 2022
 */

import com.typesafe.scalalogging.Logger
import keepid.common.constants.Domain

import scala.collection.mutable

object PrivateKeyStore {
  private val log = Logger(getClass.getName)
  private val keyStore = mutable.HashMap[Domain.RequestId, Domain.PrivateKey]()

  def storeKey(request_id: Domain.RequestId, private_key: Domain.PrivateKey): Unit = {
    log.info(f"Storing key of request with ID $request_id.")
    this.synchronized {
      keyStore.put(request_id, private_key)
    }
  }

  def containsKey(request_id: Domain.RequestId): Boolean = {
    log.debug(f"Key store contains an entry for request with ID $request_id.")
    this.synchronized {
      keyStore.contains(request_id)
    }
  }

  def getKey(request_id: Domain.RequestId): Domain.PrivateKey = {
    log.info(f"Getting key of request with ID $request_id.")
    this.synchronized {
      keyStore.getOrElse(request_id, "")
    }
  }

  def removeKey(request_id: Domain.RequestId): Unit = {
    log.info(f"Deleting key of request with ID $request_id.")
    this.synchronized {
      keyStore.remove(request_id)
    }
  }
}

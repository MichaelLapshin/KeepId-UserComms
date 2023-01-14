package keepid.services.company_request_manager

import com.typesafe.scalalogging.Logger
import keepid.common.message_broker.{ForeverConsumer, Topics}
import keepid.services.company_request_manager.CompanyRequestManagerJsonProtocol
import spray.json._

object CompanyRequestManager extends CompanyRequestManagerJsonProtocol {
  private val log = Logger(getClass.getName)
  private val consumer = new ForeverConsumer(getClass.getName)

  def runLogic(): Unit = {
    log.info(f"Subscribing encrypted data sender to topic '${Topics.EncryptedDataPrivateKeyTopic}'.")
    consumer.subscribe(Topics.EncryptedDataPrivateKeyTopic)
    consumer.pollAndProcessForever(record =>
      try {
        val data: CompanyRequestManagerReceiveData = record.value().parseJson.convertTo[CompanyRequestManagerReceiveData]
        PrivateKeyStore.storeKey(data.request_id, data.private_key)
        log.info(f"Received and stored a private key for the request with ID ${data.request_id}.")
      } catch {
        case _: Throwable => log.error("Caught an error while processing a record.")
      }
    )
    consumer.close()
  }

}

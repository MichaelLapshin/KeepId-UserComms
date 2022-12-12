package services.encrypted_data_sender

/**
 * @file: EncryptedDataSender.scala
 * @description: Define the class used to fetch and forward the encrypted user data to a company.
 * @author: KeepId Inc.
 * @date: October 1, 2022
 */

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethod, HttpMethods, HttpProtocols, HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer

import common.constants.Domain
import com.typesafe.scalalogging.Logger
import common.client_database.DBRequestManager
import common.database_structs.CompanyHost
import common.message_broker.{Consumer, Topics}
import org.apache.kafka.clients.consumer.ConsumerRecords
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters.IterableHasAsScala

object EncryptedDataSender extends EncryptedDataSenderJsonProtocol {
  implicit val system = ActorSystem() // Akka actors // TODO, look over this
  implicit val materializer = ActorMaterializer() // Akka streams // TODO, look over this
  private val log = Logger(getClass.getName)
  private val consumer = new Consumer(getClass.getName)

  def runLogic(): Unit = {
    log.info(f"Subscribing encrypted data sender to topic '$Topics.EncryptedDataTopic'.")
    consumer.subscribe(Topics.EncryptedDataTopic)

    log.info("Topic subscription successful. Starting the infinite checking loop...")
    while (true) {

      val records: ConsumerRecords[String, String] = consumer.poll()
      for (record <- records.asScala) {
        log.info("Processing a new record.")
        try {
          val data: EncryptedDataReceiveData = record.value().parseJson.toJson.convertTo[EncryptedDataReceiveData]

          // Fetch request information
          val company_host: CompanyHost = DBRequestManager.getCompanyHostInfo(data.request_id)

          // Prepares the forwarding data
          val forward_data: EncryptedDataForwardData = data.toJson.convertTo[EncryptedDataForwardData]

          // Push the http request to the company
          val http_request = HttpRequest(
            method = HttpMethods.POST,
            uri = company_host.company_host_url,
            headers = List(Authorization(BasicHttpCredentials(
              username = company_host.company_host_id.toString,
              password = company_host.company_host_token
            ))),
            entity = HttpEntity(ContentTypes.`application/json`, forward_data.toJson.compactPrint),
            protocol = HttpProtocols.`HTTP/2.0`
          )

          Http().singleRequest(http_request).foreach { response =>
            if (response.status == StatusCodes.OK) {
              log.info("Successfully sent encrypted data to company host. Response status: $.")
            } else {
              log.error(f"Failed to successfully send HTTP request to company host. Response status: ${response.status}")
            }
          }

        } catch {
          case _ =>
            log.error("Failed to process and forward the encrypted data record.")
        }
      }
    }

    consumer.close()
  }

}

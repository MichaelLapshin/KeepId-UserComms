package common.message_broker

/**
 * Defines the names of the Kafka topics used to send send information.
 */
object Topics {
  val UpdateDataTopic: String = "update_data_topic" // sys.env("KAFKA_TOPIC_UPDATE_DATA")
  val AcceptDataRequestTopic: String = "accept_data_request_topic" // sys.env("KAFKA_TOPIC_ACCEPT_REQUEST")
  val EncryptedDataTopic: String = "encrypted_data_topic"
}
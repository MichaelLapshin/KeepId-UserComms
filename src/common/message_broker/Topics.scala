package common.message_broker

/**
 * Defines the names of the Kafka topics used to send send information.
 */
object Topics {
  val UpdateDataTopic: String = "update_data_topic"
  val AcceptDataRequestTopic: String = "accept_data_request_topic"
  val EncryptedDataTopic: String = "encrypted_data_topic"
  val EncryptedDataPrivateKeyTopic: String = "encrypted_data_private_key_topic"
}
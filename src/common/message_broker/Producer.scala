package common.message_broker

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object Producer {
  lazy val props: Properties = Connection.messageBrokerProps
  lazy val producer = new KafkaProducer[String, String](props)

  /**
   * Asynchronously sends a record to the topic with a null key.
   *
   * @param topic   The topic to send to.
   * @param message The message to send.
   * @return True if the transaction was successful. False otherwise.
   */
  def send(topic: String, message: String): Boolean = {
    if (Connection.checkTopic(Connection.Topic.KeepUpdateEntryTopic, props)) {
      try {
        producer.send(new ProducerRecord[String, String](topic, null, message))
      } catch {
        case e: Exception =>
          e.printStackTrace()
          producer.close()
          return false
      }
      true
    } else {
      false
    }
  }

  /**
   * Closes the producer.
   */
  def close(): Unit = producer.close()

}

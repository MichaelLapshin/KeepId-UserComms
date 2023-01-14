package keepid.common.message_broker

/**
 * @file: Producer.scala
 * @description: Object for producing message broker messages.
 * @author: KeepId Inc.
 * @date: December 11, 2022
 */

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

class Producer {
  private val log = Logger(getClass.getName)
  private[this] val producer = new KafkaProducer[String, String](Connection.props)

  /**
   * Asynchronously sends a record to the topic with a null key.
   *
   * @param topic   The topic to send to.
   * @param message The message to send.
   * @return True if the transaction was successful. False otherwise.
   */
  def send(topic: String, message: String): Boolean = {
    try {
      log.debug(f"Sending the message '$message' to topic '$topic'.")
      producer.send(new ProducerRecord[String, String](topic, null, message))
      producer.commitTransaction()
      return true
    } catch {
      case e: Exception =>
        log.error(f"Failed to send message to topic '$topic' with error: $e")
        this.close()
        return false
    }
  }

  /**
   * Closes the producer.
   */
  def close(): Unit = {
    log.info("Kafka producer is being stopped.")
    producer.close()
  }

}

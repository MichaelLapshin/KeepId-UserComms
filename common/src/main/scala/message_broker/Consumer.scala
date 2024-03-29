package keepid.common.message_broker

/**
 * @file: Consumer.scala
 * @description: Object for consuming message broker messages.
 * @author: KeepId Inc.
 * @date: December 11, 2022
 */

import com.typesafe.scalalogging.Logger

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import java.time.Duration
import java.util

class Consumer(val Name: String) {
  protected var log = Logger(getClass.getName)
  private val pollTime = Duration.ofMillis(100)
  private[this] val consumer = new KafkaConsumer[String, String](Connection.props)

  def subscribe(topic: String): Unit = {
    log.info(f"Attempting to subscribe to topic '$topic'.")
    if (Connection.checkTopic(topic, Connection.props)) {
      consumer.subscribe(util.Collections.singletonList(topic))
    } else {
      log.error(f"Failed to find the topic '$topic'.")
      throw new Exception(f"Failed to subscribe to topic '$topic'.")
    }
  }

  def poll(): ConsumerRecords[String, String] = {
    log.info(f"Consumer '$Name' is polling.")
    consumer.poll(pollTime)
  }

  def close(): Unit = {
    log.info(f"Closing the Consumer '$Name'.")
    consumer.close()
  }

}

package common.message_broker

/**
 * @file: ForeverConsumer.scala
 * @description: Defines class for repeating the consumption and process of a kafka data stream.
 * @author: KeepId Inc.
 * @date: December 11, 2022
 */

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords}

import scala.jdk.CollectionConverters.IterableHasAsScala

class ForeverConsumer(override val Name: String) extends Consumer(Name){

  /**
   * pollAndProcess()
   * Polls from the subscribed topic and process the record on the spot.
   *
   * Warning: this function runs a while true loop. Hence, it will never exit.
   *
   * @param recordProcFunc, the function that will process the record.
   */
  def pollAndProcessForever(recordProcFunc: ConsumerRecord[String, String] => Unit) = {
    log.info(f"Starting the poll and process logic for consumer '$Name'.")
    while(true) {
      val records: ConsumerRecords[String, String] = super.poll()
      for (record <- records.asScala) {
        log.info("Processing a new record.")
        recordProcFunc(record)
      }
    }
  }
}

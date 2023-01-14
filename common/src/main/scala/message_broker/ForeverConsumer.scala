package keepid.common.message_broker

/**
 * @file: ForeverConsumer.scala
 * @description: Defines class for repeating the consumption and process of a kafka data stream.
 * @author: KeepId Inc.
 * @date: December 11, 2022
 */

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords}

import scala.jdk.CollectionConverters.IterableHasAsScala

class ForeverConsumer(override val Name: String) extends Consumer(Name){
  log = Logger(f"${getClass.getName}-$Name")

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
      var records: ConsumerRecords[String, String] = null

      try {
        records = super.poll()
      } catch {
        case _: Throwable => log.info("An error occurred while trying to poll records.")
      }

      for (record <- records.asScala) {
        log.info("Processing the next record.")
        recordProcFunc(record)
      }
    }
  }
}

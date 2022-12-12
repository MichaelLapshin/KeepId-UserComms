object TestConsumer {

}

import common.message_broker.{Connection, Topics}
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.regex.Pattern
import java.util.{Collections, Properties}
import scala.jdk.CollectionConverters.{IterableHasAsScala, SeqHasAsJava}

object KafkaConsumerSubscribeApp extends App {

  println("Running Test Consumer Main")

  /* To Test:
     kafka-console-producer.sh --topic text_topic --bootstrap-server localhost:9092
     kafka-console-consumer.sh --topic text_topic --from-beginning --bootstrap-server localhost:9092
   */


  val props: Properties = Connection.props
  props.put("group.id", "test")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "300")

  val consumer = new KafkaConsumer(props)

  val topics = List(Topics.UpdateDataTopic)
  try {
    consumer.subscribe(topics.asJava)
    while (true) {
      val records = consumer.poll(Duration.ofMillis(100))
      for (record <- records.asScala) {
        println("Topic: " + record.topic() +
          ", Key: " + record.key() +
          ", Value: " + record.value() +
          ", Offset: " + record.offset() +
          ", Partition: " + record.partition())
      }
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    consumer.close()
  }

}
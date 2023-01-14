package keepid.common.message_broker

/**
 * @file: Connection.scala
 * @description: Define methods, constants, and variables used commonly in the message broker interactions.
 * @author: KeepId Inc.
 * @date: December 11, 2022
 */

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}

import java.util.Properties
import scala.io.StdIn

object Connection {
  private val log = Logger(getClass.getName)
  private var running = false

  /**
   * Define information about the message broker.
   */
  object MsgBrokerInfo {
    val MessageBrokerUrl: String = "192.168.1.56:9093" // sys.env("MESSAGE_BROKER_URL") // TODO, remove hard-coded
    log.info(s"Loaded the message broker URL as '$MessageBrokerUrl'.")
  }

  /**
   * Function to create Kafka server properties.
   */
  def props: Properties = {
    log.trace("Fetching message broker properties.")
    val props: Properties = new Properties()

    // Common properties
    props.put("group.id", "keepid")
    props.put("bootstrap.servers", Connection.MsgBrokerInfo.MessageBrokerUrl)
    props.put("acks", "all") // Blocks until the full record has been passed
    props.put("retries", 0) // No retries if fails to send. If set to >0, there there is a chance of duplicate

    props.put("auto.create.topics.enable", "false")
    props.put("delete.topic.enable", "false")

    props.put("log.dir", "/tmp/kafka-logs")
    props.put("log.retention.hours", 24*7)

    // For Producer:
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // For Consumer:
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    // Offset commit properties
    props.put("enable.auto.commit", "false")

    props
  }

  /**
   * checkTopic()
   * Checks if a topic exists on the Kafka server already.
   *
   * @param topic      The topic to check and/or create.
   * @return True if the topic exists.
   */
  def checkTopic(topic: String, properties: Properties): Boolean = {
    log.debug(s"Checking if topic '$topic' exists on Kafka server...")

    val adminClient = AdminClient.create(properties)
    val listTopics = adminClient.listTopics
    val names = listTopics.names.get
    val contains = names.contains(topic)

    if (!contains) log.error(s"Topic '$topic' doesn't exist on Kafka server.")
    else log.debug(s"Topic '$topic' exists on Kafka server.")
    return contains
  }

  /**
   * Wait until the user ends the program to continue.
   */
  def waitUntilUserEndsServer(): Unit = {
    // Wait until the user stops the server
    running = true
    while (running) {
      val std_input: String = StdIn.readLine().toLowerCase() // let it run until user presses return

      if (std_input == "exit" || std_input == "stop") {
        running = false;
      } else {
        println("Enter 'exit' or 'stop' to stop the server.")
      }
    }
  }
}

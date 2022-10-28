package common.message_broker

import org.apache.kafka.clients.admin.{AdminClient, NewTopic}

import java.util.Properties

object Connection {

  /**
   * Define information used to facilitate the connection between servers.
   */
  object Server {
    // Message broker information
    val MessageBrokerAddress: String = "localhost"
    val MessageBrokerPort: String = "9093"

    // Certificate database address
    val DatabaseAddress: String = "localhost"
    val DatabasePort: String = "7007"
  }

  /**
   * Defines the names of the Kafka topics used to send send information.
   */
  object Topic {
    val KeepUpdateEntryTopic: String = "keep_update"
    val KeepGrantEntryTopic: String = "keep_grant"
    val ActiveRequestUpdateTopic: String = "active_request_update"
  }

  /**
   * Function to create Kafka server properties.
   */
  def messageBrokerProps: Properties = {
    val props: Properties = new Properties()

    // Common properties
    props.put("bootstrap.servers", Server.MessageBrokerAddress + ":" + Server.MessageBrokerPort)
    props.put("log.retention.hours", 168) // Number of hours to keep the log
    props.put("acks", "all") // Blocks until the full record has been passed
    props.put("retries", 0) // No retries if fails to send. If set to >0, there there is a chance of duplicate

    // For Producer:
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // For Consumer:
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  /**
   * Checks if a topic exists on the Kafka server already. Creates one if it does not exist.
   *
   * @param topic      The topic to check and/or create.
   * @param properties The properties used to create the admin client.
   * @return True if the topic exists. If the topic was created, then true would be returned.
   */
  def checkTopic(topic: String, properties: Properties, createIfMissing: Boolean = true): Boolean = { // TODO, remove "createIfMissing" since this program should not be creating its own topics
    val adminClient = AdminClient.create(properties)
    val listTopics = adminClient.listTopics
    val names = listTopics.names.get
    val contains = names.contains(topic)

    println(s"Checking if topic '$topic' exists on Kafka server...")

    if (!contains) {
      println(s"Topic '$topic' doesn't exist on Kafka server. Creating the new topic.")

      if (createIfMissing) {
        try {
          val newTopic = new NewTopic(topic, 1, 1.toShort)
          adminClient.createTopics(java.util.Collections.singletonList(newTopic))
          println("Created.")
        } finally {
          adminClient.close()
        }
      }
    }
    else {
      println(s"Topic '$topic' already exists on Kafka server.")
    }

    true //names.contains(topic)
  }
}

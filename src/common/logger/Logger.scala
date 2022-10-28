package common.logger

import akka.actor.Actor
import akka.event.Logging

// TODO, figure out how to create and use a logger

class MyLogActor extends Actor {
  val log = Logging(context.system, this)

  override def preStart() = {
    log.info("Starting the logger.")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.error(reason, "Restarting the logger due to [{}] when processing [{}]", reason.getMessage, message.getOrElse(""))
  }

  def receive = {
    case "test" => log.info("Received test")
    case x => log.warning("Received unknown message: {}", x)
  }
}

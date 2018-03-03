package com.example.impl.eventsourcing.command

import akka.Done
import com.example.models.Request
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

sealed trait LagomCommand[R] extends ReplyType[R]

case class NewCommand(message: Request) extends LagomCommand[Done]

object NewCommand {
  implicit val format: Format[NewCommand] = Json.format
}


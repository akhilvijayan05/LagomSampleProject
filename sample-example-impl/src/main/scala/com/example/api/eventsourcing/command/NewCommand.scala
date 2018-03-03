package com.example.api.eventsourcing.command

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

sealed trait LagomCommand[R] extends ReplyType[R]

case class NewCommand(message: String) extends LagomCommand[Done]


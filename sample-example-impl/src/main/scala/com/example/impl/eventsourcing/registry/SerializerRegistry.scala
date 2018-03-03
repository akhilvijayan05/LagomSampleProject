package com.example.impl.eventsourcing.registry

import com.example.impl.eventsourcing.command.NewCommand
import com.example.impl.eventsourcing.event.NewEvent
import com.example.impl.eventsourcing.state.NewState
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable

object SerializerRegistry extends JsonSerializerRegistry {

  override def serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
    JsonSerializer[NewCommand],
    JsonSerializer[NewEvent],
    JsonSerializer[NewState]
  )
}

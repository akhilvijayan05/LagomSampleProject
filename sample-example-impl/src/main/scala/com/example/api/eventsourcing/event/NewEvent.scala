package com.example.api.eventsourcing.event

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}


sealed trait LagomEvent extends AggregateEvent[LagomEvent] {
  def aggregateTag = LagomEvent.Tag
}

object LagomEvent {
  val Tag = AggregateEventTag[LagomEvent]
}

case class NewEvent(message: String) extends LagomEvent

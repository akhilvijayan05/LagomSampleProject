package com.example.impl.eventsourcing.event

import com.example.models.Request
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}


sealed trait LagomEvent extends AggregateEvent[LagomEvent] {
  def aggregateTag = LagomEventTag.Tag
}

object LagomEventTag {
  val Tag = AggregateEventTag[LagomEvent]
}

case class NewEvent(message: Request) extends LagomEvent

object NewEvent {
  implicit val format: Format[NewEvent] = Json.format
}

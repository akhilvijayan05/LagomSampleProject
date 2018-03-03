package com.example.impl.eventsourcing.state

import com.example.models.Request
import play.api.libs.json.{Format, Json}


case class NewState(message: Request, timestamp: String)

object NewState {
  implicit val format: Format[NewState] = Json.format
}

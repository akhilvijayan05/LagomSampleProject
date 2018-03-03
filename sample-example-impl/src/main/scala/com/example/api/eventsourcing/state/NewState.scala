package com.example.api.eventsourcing.state

import play.api.libs.json.{Format, Json}


case class NewState(message: String, timestamp: String)

object NewState {
  implicit val format: Format[NewState] = Json.format
}

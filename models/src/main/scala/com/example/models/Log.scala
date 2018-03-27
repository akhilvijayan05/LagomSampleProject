package com.example.models

import play.api.libs.json.{Format, Json}


case class Log(timestamp: String, request: Request, response: Response)

object Log {
  implicit val format: Format[Log] = Json.format
}

package com.example.models

import play.api.libs.json.{Format, Json}


case class Response(message: String)

object Response {
  implicit val format: Format[Response] = Json.format
}
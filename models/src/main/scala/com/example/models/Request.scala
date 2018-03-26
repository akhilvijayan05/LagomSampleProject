package com.example.models

import play.api.libs.json.{Format, Json}

case class Request(id: String, message: String = "")

object Request {
  implicit val format: Format[Request] = Json.format
}

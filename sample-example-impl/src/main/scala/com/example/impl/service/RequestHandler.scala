package com.example.impl.service

import java.io.IOException

import com.example.impl.es.JestClient
import com.example.models.{Request, Response}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RequestHandler(jestClient: JestClient) {

  def processRequest(request: Request) = {

    try {

      jestClient.createDocument(request)
      Future(Response(s"Got ${request.message}"))
    } catch {

      case ioException: IOException =>
        Future(Response("Error in our end. PLease try later " + ioException.getMessage))
      case exception: Exception =>
        Future(Response("Error in our end. PLease try later " + exception.getMessage))
    }
  }
}

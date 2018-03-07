package com.example.impl.service

import java.io.IOException

import com.example.api.RestServiceApi
import com.example.impl.es.JestClient
import com.example.impl.eventsourcing.command.NewCommand
import com.example.impl.eventsourcing.entity.Entity
import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.{ExecutionContext, Future}


class RestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, jestClient: JestClient)(implicit ec: ExecutionContext) extends RestServiceApi {

  override def postRequest: ServiceCall[Request, Response] = ServiceCall { request: Request =>

    val ref = persistentEntityRegistry.refFor[Entity](request.id)
    val response = ref.ask(NewCommand(request))

    try {

      jestClient.createDocument(request)
      Future(Response(s"Got ${request.message}"))
    } catch {

      case ioException: IOException =>
        Future(Response("Error in our end. PLease try later " + ioException.getMessage))
      case exception: Exception =>
        Future(Response("Error in our end. PLease try later "+ exception.getMessage))
    }
  }
}
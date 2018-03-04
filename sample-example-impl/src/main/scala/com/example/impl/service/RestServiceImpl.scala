package com.example.impl.service

import com.example.api.RestServiceApi
import com.example.impl.eventsourcing.command.NewCommand
import com.example.impl.eventsourcing.entity.Entity
import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext


class RestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends RestServiceApi {

  override def postRequest: ServiceCall[Request, Response] = ServiceCall { request: Request =>
    val ref = persistentEntityRegistry.refFor[Entity](request.id)
    val response = ref.ask(NewCommand(request))
    response.map(_ => Response(s"Got ${request.message}"))
  }
}

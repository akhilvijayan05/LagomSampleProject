package com.example.impl.service

import akka.Done
import com.example.api.RestServiceApi
import com.example.impl.es.JestClient
import com.example.impl.eventsourcing.command.NewCommand
import com.example.impl.eventsourcing.entity.Entity
import com.example.impl.logs.LogHandler
import com.example.models.{Log, Request, Response}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.{ExecutionContext, Future}


class RestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, jestClient: JestClient)(implicit ec: ExecutionContext) extends RestServiceApi {

  override def postRequest: ServiceCall[Request, Response] = ServiceCall { request: Request =>

    val ref = persistentEntityRegistry.refFor[Entity](request.id)
    val logHandler = new LogHandler()
    val result = ref.ask(NewCommand(request))
    result.map { status =>
      if (status == Done) {
        jestClient.createDocument(request)
        logHandler.storeLogs(Log("", request, Response(request.message)), "NewTopic")
        Response(s"Got ${request.message}")
      } else {
        Response("Server Error. Please try later..")
      }
    }

  }

  override def getRequest: ServiceCall[Request, Request] = ServiceCall { request: Request =>

    Future(jestClient.getDocument(request))
  }
}
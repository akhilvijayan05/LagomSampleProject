package com.example.api.service

import com.example.api.RestServiceApi
import com.example.models.Request
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry


class RestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends RestServiceApi {

  override def postRequest(id: String) = ServiceCall { request: Request =>

    val ref = persistentEntityRegistry.refFor[HelloEntity](id)
    ref.ask(Hello(id, None))
  }
}

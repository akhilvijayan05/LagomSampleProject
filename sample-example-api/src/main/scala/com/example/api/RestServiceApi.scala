package com.example.api

import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait RestServiceApi extends Service {

  override final def descriptor = {
    import Service._
    named("sample-example")
      .withCalls(
        pathCall("/example", postRequest)
      )
      .withAutoAcl(true)
  }

  def postRequest: ServiceCall[Request, Response]
}

package com.example.impl.service

import com.example.api.RestServiceApi
import com.example.impl.loader.Application
import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}


class RestServiceImplSpec(persistentEntity: PersistentEntityRegistry) extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new Application(ctx) with LocalServiceLocator
  }

  lazy val client = server.serviceClient.implement[RestServiceApi]

  override def afterAll(): Unit = {
    server.stop()
  }

  "RestServiceImpl" should {
    "Able to send post request" in {
      val request = Request("1", "New Message")
      client.postRequest.invoke(request).map { response =>
        response should ===(Response("New Message"))
      }
    }
  }

}

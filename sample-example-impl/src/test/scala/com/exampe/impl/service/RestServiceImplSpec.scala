package com.exampe.impl.service

import com.example.api.RestServiceApi
import com.example.impl.es.JestClient
import com.example.impl.loader.Application
import com.example.models.Request
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.mockito.Mockito.when
import org.mockito.Mockito.verify
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class RestServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new Application(ctx) with LocalServiceLocator
  }
  lazy val client = server.serviceClient.implement[RestServiceApi]
  val mockedJestClient = mock[JestClient]


  "The RestServiceImpl" should {
    "get the correct response for the request" in {
      val request = Request("1111", "Test Message")
      when(mockedJestClient.createDocument(request)) thenReturn "1111"
      client.postRequest.invoke(request).map { response =>
        verify(mockedJestClient).createDocument(request)
        response.message should ===("Got Test Message")
      }
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}

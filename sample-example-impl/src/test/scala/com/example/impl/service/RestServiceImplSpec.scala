package com.example.impl.service

import com.example.api.RestServiceApi
import com.example.impl.elasticsearch.JestClient
import com.example.impl.kafkaprocessing.Producer
import com.example.impl.loader.Application
import com.example.impl.logs.LogHandler
import com.example.models.{Log, Request, Response}
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.mockito.Mockito.{verify, when}
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class RestServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

//  lazy val mockedJestClient = mock[JestClient]
  lazy val mockedLogHandler = mock[LogHandler]
  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new Application(ctx) with LocalServiceLocator {
//      override lazy val jestClient = mockedJestClient
      override lazy val logHandler = mockedLogHandler
    }
  }
  lazy val client = server.serviceClient.implement[RestServiceApi]

  "The RestServiceImpl" should {
    "get the correct response for the request" in {
      val request = Request("1111", "Test Message")
      val response = Response("Test Message")
//      when(mockedJestClient.createDocument(request)) thenReturn "1111"
      when(mockedLogHandler.storeLogs(anyString(), any(request.getClass), any(response.getClass), anyString())) thenReturn "TesTopic"
      client.postRequest.invoke(request).map { response =>
//        verify(mockedJestClient).createDocument(request)
        verify(mockedLogHandler).storeLogs(anyString(), any(request.getClass), any(response.getClass), anyString())
        response.message should ===("Got Test Message")
      }
    }
  }

  "The RestServiceImpl" should {
    "fetch the stored data from elasticsearch" in {
      val request = Request("1111")
//      when(mockedJestClient.getDocument(request)) thenReturn Request("1111", "Test Message")
      client.getRequest.invoke(request).map { response =>
//        verify(mockedJestClient).getDocument(request)
        response.message should ===("Test Message")
      }
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}

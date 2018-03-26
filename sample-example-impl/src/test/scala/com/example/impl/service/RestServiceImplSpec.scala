package com.example.impl.service

import com.example.api.RestServiceApi
import com.example.impl.es.JestClient
import com.example.impl.loader.Application
import com.example.models.Request
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class RestServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  lazy val mockedJestClient = mock[JestClient]
  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new Application(ctx) with LocalServiceLocator {
      override lazy val jestClient = mockedJestClient
    }
  }
  lazy val client = server.serviceClient.implement[RestServiceApi]


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

  "The RestServiceImpl" should {
    "fetch the stored data from elasticsearch" in {
      val request = Request("1111")
      when(mockedJestClient.getDocument(request)) thenReturn Request("1111", "Test Message")
      client.getRequest.invoke(request).map { response =>
        verify(mockedJestClient).getDocument(request)
        response.message should ===("Test Message")
      }
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}

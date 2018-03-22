package com.example.impl.service

import com.example.impl.es.JestClient
import com.example.impl.service.RequestHandler
import com.example.models.Request
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}


class RequestHandlerSpec extends AsyncWordSpec with Matchers with MockitoSugar {

  val mockedJestClient = mock[JestClient]
  val requestHandler = new RequestHandler(mockedJestClient)
  "The RequestHandler" should {
    "get the correct response for the request" in {
      val request = Request("1111", "Test Message")
      when(mockedJestClient.createDocument(request)) thenReturn "1111"
      requestHandler.processRequest(request).map { response =>
        verify(mockedJestClient).createDocument(request)
        response.message should ===("Got Test Message")
      }
    }
  }
}

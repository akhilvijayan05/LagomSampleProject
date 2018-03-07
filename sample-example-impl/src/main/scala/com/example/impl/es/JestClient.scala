package com.example.impl.es

import java.io.IOException

import com.example.models.Request
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Delete, Get, Index, Update}
import play.api.libs.json.Json


class JestClient {

  val INDEX = "sample_index"
  val ADDRESS = "http://localhost:9200"
  private val jestClientFactory: JestClientFactory = new JestClientFactory()
  jestClientFactory.setHttpClientConfig(new HttpClientConfig
  .Builder(ADDRESS)
    .multiThreaded(true)
    .build())

  private val jestClient = jestClientFactory.getObject()

  def createDocument(request: Request): String = {
    val jsonData = Json.toJson(request).toString()
    val documentResult = jestClient.execute(new Index.Builder(jsonData).index(INDEX).`type`(request.getClass.getSimpleName.toLowerCase).id(request.id).build())
    documentResult.getId
  }

  def getDocument(request: Request): Request = {

    val getResult = new Get.Builder(INDEX, request.id).`type`(request.getClass.getSimpleName.toLowerCase).build()
    val documentResult = jestClient.execute(getResult)
    val requestData = Json.parse(documentResult.getSourceAsString).as[Request]
    requestData
  }

  def updateDocument(request: Request) : Boolean = {

    val jsonData = Json.toJson(request).toString()
    jestClient.execute(new Update.Builder(jsonData).index(INDEX).`type`(request.getClass.getSimpleName.toLowerCase).id(request.id).build())
    true
  }

  def deleteDocument(request: Request): Boolean = {

    jestClient.execute(new Delete.Builder(request.id).index(INDEX).`type`(request.getClass.getSimpleName.toLowerCase).build())
    true
  }
}

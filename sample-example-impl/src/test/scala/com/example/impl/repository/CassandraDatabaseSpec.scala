package com.example.impl.repository

import com.example.impl.loader.Application
import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import play.api.libs.json.Json


class CassandraDatabaseSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new Application(ctx) with LocalServiceLocator
  }

  private val session = server.application.cassandraSession

  override def beforeAll(): Unit = {
    cassandraDatabase.createTable()
    cassandraDatabase.createPreparedStatement()
  }
  override def afterAll(): Unit = {
    server.stop()
  }

  val cassandraDatabase = server.application.cassandraDatabase
  cassandraDatabase.createTable()

  "CassandraDatabase" should {
    "insert data" in {
      val request = Request("1", "New Message")
      cassandraDatabase.insertData(request).map{result =>
        Json.parse(result.head.getString(0)).as[Response].message should === ("New Message")}
    }
  }
}

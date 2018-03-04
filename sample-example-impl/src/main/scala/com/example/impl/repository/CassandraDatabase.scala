package com.example.impl.repository

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.example.models.Request
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future, Promise}


class CassandraDatabase(cassandraSession: CassandraSession)(implicit executionContext: ExecutionContext) {

  private val writeTitlePromise = Promise[PreparedStatement]

  def createTable(): Future[Done] = {
    cassandraSession.executeCreateTable("CREATE TABLE IF NOT EXISTS request (id TEXT, message TEXT, PRIMARY KEY (id))")
  }

  def createPreparedStatement(): Future[Done] = {
    val preparedStatement = cassandraSession.prepare("INSERT INTO request JSON ?")
    writeTitlePromise.completeWith(preparedStatement)
    preparedStatement.map(_ => Done)
  }

  def insertData(request: Request): Future[List[BoundStatement]] = {
    writeTitle.map { preparedStatement =>
      List(preparedStatement.bind(Json.toJson(request).toString()))
    }
  }

  private def writeTitle: Future[PreparedStatement] = writeTitlePromise.future
}

package com.example.impl.loader

import com.example.api.RestServiceApi
import com.example.impl.elasticsearch.JestClient
import com.example.impl.eventsourcing.entity.Entity
import com.example.impl.eventsourcing.event.LagomEvent
import com.example.impl.eventsourcing.registry.SerializerRegistry
import com.example.impl.logs.LogHandler
import com.example.impl.processor.CassandraReadSideProcessor
import com.example.impl.repository.CassandraDatabase
import com.example.impl.service.RestServiceImpl
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class ApplicationLoader extends LagomApplicationLoader {

  override def loadDevMode(context: LagomApplicationContext) =
    new Application(context) with LagomDevModeComponents

  override def load(context: LagomApplicationContext) =
    new Application(context) {
      override def serviceLocator = ServiceLocator.NoServiceLocator
    }

  override def describeService = Some(readDescriptor[RestServiceApi])
}

abstract class Application(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {


//  lazy val jestClient = wire[JestClient]
  lazy val logHandler = wire[LogHandler]
  override lazy val lagomServer = serverFor[RestServiceApi](wire[RestServiceImpl])
  override lazy val jsonSerializerRegistry = SerializerRegistry
  persistentEntityRegistry.register(wire[Entity])
  val cassandraDatabase = new CassandraDatabase(cassandraSession)
  readSide.register[LagomEvent](new CassandraReadSideProcessor(cassandraReadSide, cassandraDatabase))
}
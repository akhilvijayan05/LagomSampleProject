package com.example.impl.loader

import com.example.api.RestServiceApi
import com.example.impl.eventsourcing.entity.Entity
import com.example.impl.service.RestServiceImpl
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._
import com.example.impl.eventsourcing.registry.SerializerRegistry

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

  override lazy val lagomServer = serverFor[RestServiceApi](wire[RestServiceImpl])
  override lazy val jsonSerializerRegistry = SerializerRegistry
  persistentEntityRegistry.register(wire[Entity])
}
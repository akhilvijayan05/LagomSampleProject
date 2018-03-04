package com.example.impl.processor

import com.example.impl.eventsourcing.event.{LagomEvent, NewEvent}
import com.example.impl.repository.CassandraDatabase
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraReadSide
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, ReadSideProcessor}

import scala.concurrent.ExecutionContext


class CassandraReadSideProcessor(readSide: CassandraReadSide, cassandraDatabase: CassandraDatabase)(implicit ec: ExecutionContext) extends ReadSideProcessor[LagomEvent] {

  override def buildHandler(): ReadSideProcessor.ReadSideHandler[LagomEvent] = {
    readSide.builder[LagomEvent]("offset")
      .setGlobalPrepare(() => cassandraDatabase.createTable())
      .setPrepare(_ => cassandraDatabase.createPreparedStatement())
      .setEventHandler[NewEvent](e => cassandraDatabase.insertData(e.event.message))
      .build()
  }

  override def aggregateTags: Set[AggregateEventTag[LagomEvent]] =
    Set(LagomEvent.Tag)

}

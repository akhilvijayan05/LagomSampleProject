package com.example.api.eventsourcing.entity

import java.time.LocalDateTime

import akka.Done
import com.example.api.eventsourcing.command.{LagomCommand, NewCommand}
import com.example.api.eventsourcing.event.{LagomEvent, NewEvent}
import com.example.api.eventsourcing.state.NewState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class Entity extends PersistentEntity {

  override type Command = LagomCommand[_]
  override type Event = LagomEvent
  override type State = NewState

  override def initialState: NewState = NewState("initial", LocalDateTime.now.toString)

  override def behavior: Behavior = {
    case NewState(message, _) => Actions().onCommand[NewCommand, Done] {

      case (NewCommand(newMessage), ctx, _) =>
        ctx.thenPersist(
          NewEvent(newMessage)
        ) { _ =>
          ctx.reply(Done)
        }

    }.onEvent {
      case (NewEvent(newMessage), _) =>
        NewState(newMessage, LocalDateTime.now().toString)
    }
  }
}

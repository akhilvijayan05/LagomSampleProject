package com.example.impl.eventsourcing.entity

import java.time.LocalDateTime

import akka.Done
import com.example.impl.eventsourcing.command.{LagomCommand, NewCommand}
import com.example.impl.eventsourcing.event.{LagomEvent, NewEvent}
import com.example.impl.eventsourcing.state.NewState
import com.example.models.Request
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class Entity extends PersistentEntity {

  override type Command = LagomCommand[_]
  override type Event = LagomEvent
  override type State = NewState

  override def initialState: NewState = NewState(Request("none","initial"), LocalDateTime.now.toString)

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

package dev.lizainslie.moeka.core.task.events

import dev.lizainslie.moeka.core.event.BotEvent
import dev.lizainslie.moeka.core.event.EventSource

class TaskDispatchedEvent : BotEvent {
    override val source = EventSource.CORE


}
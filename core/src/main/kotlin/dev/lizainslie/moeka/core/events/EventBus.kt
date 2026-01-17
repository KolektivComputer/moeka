package dev.lizainslie.moeka.core.events

import kotlin.reflect.KClass

interface EventBus {
    fun <TEvent : BotEvent> post(event: TEvent)
    fun <TEvent : BotEvent> register(type: KClass<TEvent>, listener: EventListener<TEvent>)
}
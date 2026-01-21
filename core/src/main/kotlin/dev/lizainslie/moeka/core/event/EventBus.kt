package dev.lizainslie.moeka.core.event

import dev.lizainslie.moeka.core.modules.AbstractModule
import kotlin.reflect.KClass

interface EventBus {
    fun <TEvent : BotEvent> post(event: TEvent)
    fun <TEvent : BotEvent> attach(
        type: KClass<TEvent>,
        listener: EventListener<TEvent>,
        module: AbstractModule? = null,
    )
}
package dev.lizainslie.moeka.core.event

import dev.lizainslie.moeka.core.modules.AbstractModule
import kotlin.reflect.KClass

class DefaultEventBus : EventBus {
    private val listeners = mutableMapOf<KClass<*>, MutableList<AttachedEventListener<*>>>()

    override fun <TEvent : BotEvent> post(event: TEvent) {
        TODO("Not yet implemented")
    }

    override fun <TEvent : BotEvent> attach(
        type: KClass<TEvent>,
        listener: EventListener<TEvent>,
        module: AbstractModule?,
    ) {
        listeners.getOrPut(type) { mutableListOf() }
            .add(AttachedEventListener(
                listener,
                module = module,
            ))
    }
}
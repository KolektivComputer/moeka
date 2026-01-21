package dev.lizainslie.moeka.core.event

import dev.lizainslie.moeka.core.modules.AbstractModule
import java.util.concurrent.atomic.AtomicInteger

data class AttachedEventListener<TEvent : BotEvent>(
    val listener: EventListener<TEvent>,
    val id: Int = idx.incrementAndGet(),
    val module: AbstractModule? = null,
) {
    companion object {
        private val idx = AtomicInteger(0)
    }
}

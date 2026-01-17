package dev.lizainslie.moeka.core.commands.argument

fun interface PlatformArgumentParseFn<out T> {
    fun parse(value: Any): T
}

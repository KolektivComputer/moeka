package dev.lizainslie.moeka.core.logging

import dev.lizainslie.moeka.core.modules.AbstractPlugin
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import dev.lizainslie.moeka.core.platforms.PlatformKey
import org.slf4j.MDC

suspend fun <T> suspendLogTag(
    tag: String,
    block: suspend () -> T,
): T {
    MDC.put("tag", tag)
    val result = block()
    MDC.remove("tag")
    return result
}

fun <T> logTag(
    tag: String,
    block: () -> T,
): T {
    MDC.put("tag", tag)
    val result = block()
    MDC.remove("tag")
    return result
}

suspend fun <T> suspendLogPlugin(
    module: AbstractPlugin,
    block: suspend () -> T,
): T {
    MDC.put("plugin", module.name)
    val result = block()
    MDC.remove("plugin")
    return result
}

fun <T> logPlugin(
    plugin: AbstractPlugin,
    block: () -> T,
): T {
    MDC.put("plugin", plugin.name)
    val result = block()
    MDC.remove("plugin")
    return result
}

suspend fun <T> suspendLogPlatform(
    platform: PlatformKey,
    block: suspend () -> T,
): T {
    MDC.put("platform", platform.key)
    val result = block()
    MDC.remove("platform")
    return result
}

fun <T> logPlatform(
    platform: PlatformKey,
    block: () -> T,
): T {
    MDC.put("platform", platform.key)
    val result = block()
    MDC.remove("platform")
    return result
}

suspend fun <T> suspendLogPlatform(
    platform: AnyPlatformAdapter,
    block: suspend () -> T,
) = suspendLogPlatform(platform.key, block)

fun <T> logPlatform(
    platform: AnyPlatformAdapter,
    block: () -> T,
) = logPlatform(platform.key, block)

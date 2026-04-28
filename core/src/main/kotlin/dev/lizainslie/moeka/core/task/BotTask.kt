package dev.lizainslie.moeka.core.task

fun interface BotTask {
    suspend fun work()
}
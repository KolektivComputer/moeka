package dev.lizainslie.moeka.core.logging

import dev.lizainslie.moeka.core.config.ConfigService

object Logging {
    val config by ConfigService.config<LoggingConfig>()

    fun init() {
        syncLevel(config.level)
    }

    fun syncLevel(level: String) {
        System.setProperty("log.level", level.uppercase())
    }
}

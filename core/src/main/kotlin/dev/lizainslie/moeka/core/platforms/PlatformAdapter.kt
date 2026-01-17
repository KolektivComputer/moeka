package dev.lizainslie.moeka.core.platforms

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.commands.PlatformCommandConfig
import dev.lizainslie.moeka.core.commands.RootCommand
import dev.lizainslie.moeka.core.commands.argument.PlatformArgumentParseFn
import dev.lizainslie.moeka.core.logging.suspendLogPlatform
import dev.lizainslie.moeka.core.modules.AbstractModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class PlatformAdapter<
    TCommandConfig : PlatformCommandConfig,
>(
    val key: PlatformKey,
    val displayName: String,
) {
    protected val log: Logger get() = LoggerFactory.getLogger(this::class.java)

    open val channelArgumentParser: PlatformArgumentParseFn<PlatformId>? = null
    open val roleArgumentParser: PlatformArgumentParseFn<PlatformId>? = null
    open val userArgumentParser: PlatformArgumentParseFn<PlatformId>? = null

    lateinit var bot: Bot

    open suspend fun initialize(bot: Bot) {
        this.bot = bot
    }

    suspend fun <T> suspendLogPlatform(block: suspend () -> T) = suspendLogPlatform(key, block)

    abstract suspend fun start(bot: Bot)

    abstract suspend fun stop()

    abstract suspend fun registerCommand(
        command: RootCommand,
        module: AbstractModule,
    )

    abstract suspend fun unregisterCommand(
        command: RootCommand,
        module: AbstractModule,
    )

    abstract fun createEmptyCommandConfig(): TCommandConfig
}

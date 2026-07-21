package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.commands.argument.ArgumentDescriptor
import dev.lizainslie.moeka.core.manual.Manual
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import dev.lizainslie.moeka.core.platforms.PlatformKey
import kotlin.reflect.KClass

abstract class BaseCommand(
    val name: String,
    val description: String,
) {
    abstract val rootCommand: RootCommand

    open val arguments: List<ArgumentDescriptor<*>> = emptyList()

    abstract val handlers: Map<KClass<*>, CommandHandler<*>>

    fun supportsPlatform(key: PlatformKey) = rootCommand.platforms.containsKey(key)

    fun supportsPlatform(platform: AnyPlatformAdapter) = supportsPlatform(platform.key)
}

abstract class RootCommand(
    name: String,
    description: String,
) : BaseCommand(name, description) {
    abstract val platforms: Map<PlatformKey, PlatformCommandConfig>
    override val rootCommand = this
    open val communityOnly: Boolean = false
    open val manual: Manual? = null

    open val subCommands: List<SubCommand> = emptyList()
}

abstract class SubCommand(
    name: String,
    description: String,
    parent: BaseCommand,
) : BaseCommand(name, description) {
    override val rootCommand = parent.rootCommand
}

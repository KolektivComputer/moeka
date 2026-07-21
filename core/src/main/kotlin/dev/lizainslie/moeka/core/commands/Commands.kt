package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.config.Configs
import dev.lizainslie.moeka.core.data.entities.DeveloperOptions
import dev.lizainslie.moeka.core.logging.suspendLogPlugin
import dev.lizainslie.moeka.core.logging.suspendLogPlatform
import dev.lizainslie.moeka.core.logging.suspendLogTag
import dev.lizainslie.moeka.core.plugins.AbstractPlugin
import dev.lizainslie.moeka.core.plugins.PluginVisibility
import dev.lizainslie.moeka.core.platforms.UnsupportedPlatformException
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.slf4j.LoggerFactory

class Commands(
    private val bot: Bot,
) {
    val parsingConfig by Configs.config<CommandParsingConfig>()
    val commands = mutableListOf<CommandRegistration>()
    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun registerCommand(
        command: RootCommand,
        module: AbstractPlugin,
    ) {
        suspendLogPlugin(module) {
            log.info("Registering command: '${command.name}'.")
            commands.add(CommandRegistration(command, module))

            bot.eachPlatform {
                suspendLogPlatform(it) {
                    if (module.supportsPlatform(it) &&
                        command.supportsPlatform(it)
                    ) {
                        it.registerCommand(command, module)
                    }
                }
            }
        }
    }

    suspend fun unregisterPluginCommands(plugin: AbstractPlugin) {
        suspendLogPlugin(plugin) {
            log.info("Unregistering commands for module '${plugin.name}'.")
            val toRemove = commands.filter { it.plugin == plugin }
            commands.removeAll(toRemove)

            bot.eachPlatform {
                suspendLogPlatform(it) {
                    for (reg in toRemove) {
                        if (plugin.supportsPlatform(it) && reg.command.supportsPlatform(it)) {
                            it.unregisterCommand(reg.command, plugin)
                        }
                    }
                }
            }
        }
    }

    suspend fun registerPluginCommands(plugin: AbstractPlugin) {
        suspendLogPlugin(plugin) {
            log.info("Registering commands for plugin '${plugin.name}'.")
            for (command in plugin.commands) {
                registerCommand(command, plugin)
            }
        }
    }

    fun getRegistration(commandName: String): CommandRegistration? = commands.find { it.command.rootCommand.name == commandName }

    private suspend fun respondUnsupportedPlatform(
        handlingCommand: BaseCommand,
        context: CommandContext,
    ) {
        context.respondError("The ${handlingCommand.rootCommand.name} command is not supported on ${context.platform.displayName}.")
    }

    private suspend fun respondUnsupportedPlatform(
        handlingCommand: BaseCommand,
        context: CommandContext,
        exception: UnsupportedPlatformException,
    ) {
        context.respondError(
            "The ${
                handlingCommand.rootCommand.name
            } command is not supported on ${
                exception.currentPlatform.displayName
            }. It can be used on: ${
                exception.allowedPlatforms.joinToString(
                    ", ",
                ) { it.displayName }
            }",
        )
    }

    suspend fun dispatchCommand(
        handlingCommand: BaseCommand,
        context: CommandContext,
    ) {
        suspendLogPlugin(context.plugin) {
            suspendLogPlatform(context.platform) {
                if (!context.plugin.supportsPlatform(context.platform)) {
                    respondUnsupportedPlatform(handlingCommand, context)
                    return@suspendLogPlatform
                }

                if (context.plugin.visibility == PluginVisibility.DEVELOPER && !context.callerIsDeveloper()) {
                    return@suspendLogPlatform // exit silently.
                }

                if (handlingCommand.rootCommand.communityOnly && !context.isInCommunity) {
                    context.respondError("The ${handlingCommand.rootCommand.name} command can only be used in communities.")
                    return@suspendLogPlatform
                }

                val devOpts =
                    suspendTransaction {
                        DeveloperOptions.getDeveloperOptions(context.callerId)
                    }

                try {
                    log.info("Handling command: '${handlingCommand.rootCommand.name}' on platform '${context.platform.displayName}'.")
                    suspendLogTag("command: ${handlingCommand.rootCommand.name}") {
                        handlingCommand.handle(context)
                    }
                } catch (exc: UnsupportedPlatformException) {
                    respondUnsupportedPlatform(handlingCommand, context, exc)
                } catch (exc: Exception) {
                    log.error("Handling command '${handlingCommand.rootCommand.name}' failed with ${exc.message}: ", exc)
                    if (devOpts != null) context.respondException(exc)
                }

                if (devOpts != null && devOpts.contextDebug) {
                    log.debug("Dumping context.")
                    context.dump()
                }
            }
        }
    }
}

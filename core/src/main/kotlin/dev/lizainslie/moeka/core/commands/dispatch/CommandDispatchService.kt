package dev.lizainslie.moeka.core.commands.dispatch

import dev.lizainslie.moeka.core.commands.BaseCommand
import dev.lizainslie.moeka.core.commands.CommandContext
import dev.lizainslie.moeka.core.commands.CommandHandler
import dev.lizainslie.moeka.core.data.entities.DeveloperOptions
import dev.lizainslie.moeka.core.logging.suspendLogPlatform
import dev.lizainslie.moeka.core.logging.suspendLogPlugin
import dev.lizainslie.moeka.core.logging.suspendLogTag
import dev.lizainslie.moeka.core.platforms.UnsupportedPlatformException
import dev.lizainslie.moeka.core.plugins.types.PluginVisibility
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommandDispatchService : KoinComponent {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun respondUnsupportedPlatform(
        handlingCommand: BaseCommand,
        context: CommandContext,
    ) {
        context.respondError("The ${handlingCommand.rootCommand.name} command is not supported on ${context.platform.displayName}.")
    }

    suspend fun respondUnsupportedPlatform(
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

    suspend inline fun<reified TContext : CommandContext> dispatchCommand(
        handlingCommand: BaseCommand,
        context: TContext,
    ) {
        suspendLogPlugin(context.plugin) {
            suspendLogPlatform(context.platform) {
                context.plugin?.let { plugin ->
                    if (plugin.supportsPlatform(context.platform)) {
                        respondUnsupportedPlatform(handlingCommand, context)
                        return@suspendLogPlatform
                    }

                    if (plugin.visibility == PluginVisibility.DEVELOPER && !context.callerIsDeveloper()) {
                        return@suspendLogPlatform // exit silently.
                    }
                }

                if (!handlingCommand.rootCommand.platforms.containsKey(context.platform.key)) {
                    respondUnsupportedPlatform(handlingCommand, context)
                    return@suspendLogPlatform
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
                        @Suppress("UNCHECKED_CAST")
                        val handler = handlingCommand.handlers[TContext::class] as? CommandHandler<TContext> ?: run {
                            return@suspendLogTag
                        }

                        handler(context)
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
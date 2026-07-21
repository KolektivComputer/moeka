package dev.lizainslie.moeka.core.commands.registration

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.commands.BaseCommand
import dev.lizainslie.moeka.core.commands.CommandContext
import dev.lizainslie.moeka.core.commands.CommandParsingConfig
import dev.lizainslie.moeka.core.commands.RootCommand
import dev.lizainslie.moeka.core.config.ConfigService
import dev.lizainslie.moeka.core.logging.suspendLogPlugin
import dev.lizainslie.moeka.core.logging.suspendLogPlatform
import dev.lizainslie.moeka.core.platforms.PlatformKey
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import dev.lizainslie.moeka.core.platforms.UnsupportedPlatformException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.slf4j.LoggerFactory

class CommandRegistrationService : KoinComponent {
    private val bot by inject<Bot>()
    val configService by inject<ConfigService>()

    val parsingConfig by configService.config<CommandParsingConfig>()
    val commands = mutableListOf<CommandRegistration>()
    val log = LoggerFactory.getLogger(javaClass)

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

    fun createCommandExecutionScope(platform: PlatformKey, commandName: String) = getKoin().createScope("", named<CommandContext>())

    suspend fun registerPluginCommands(plugin: AbstractPlugin) {
        suspendLogPlugin(plugin) {
            log.info("Registering commands for plugin '${plugin.name}'.")
            for (command in plugin.commands) {
                registerCommand(command, plugin)
            }
        }
    }

    fun getRegistration(commandName: String): CommandRegistration? = commands.find { it.command.rootCommand.name == commandName }
}

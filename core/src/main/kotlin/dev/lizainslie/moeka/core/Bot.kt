package dev.lizainslie.moeka.core

import dev.lizainslie.moeka.core.commands.registration.CommandRegistrationService
import dev.lizainslie.moeka.core.config.ConfigService
import dev.lizainslie.moeka.core.data.DbContext
import dev.lizainslie.moeka.core.data.tables.DeveloperOptionsTable
import dev.lizainslie.moeka.core.data.tables.PluginCommunitySettingsTable
import dev.lizainslie.moeka.core.data.tables.ModuleSwitchTable
import dev.lizainslie.moeka.core.data.tables.PluginVersionTable
import dev.lizainslie.moeka.core.fs.BotFs
import dev.lizainslie.moeka.core.fs.TempFs
import dev.lizainslie.moeka.core.logging.Logging
import dev.lizainslie.moeka.core.manual.ManualRegistryService
import dev.lizainslie.moeka.core.plugins.registry.PluginRegistryService
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Bot(
) : KoinComponent {
    val plugins by inject<PluginRegistryService>()
    val commands by inject<CommandRegistrationService>()
    val configs by inject<ConfigService>()
    val manPages = ManualRegistryService()
    val fs by inject<BotFs>()
    val tempFs by inject<TempFs>()

    val platformAdapters = mutableListOf<AnyPlatformAdapter>()

    init {
        // generate the base folder structure if it doesn't exist
        fs.generateBaseStructure()

        // check to ensure that configs are valid
        configs.checkConfigs()

        Logging.init()

        DbContext.connect()
        DbContext.tables += PluginVersionTable
        DbContext.tables += ModuleSwitchTable
        DbContext.tables += DeveloperOptionsTable
        DbContext.tables += PluginCommunitySettingsTable
        DbContext.migrate()
    }

    suspend fun loadPlugins() {
        plugins.loadJarPlugins()
    }

    fun enablePlatforms(vararg platforms: AnyPlatformAdapter) {
        platforms.forEach { adapter ->
            if (platformAdapters.none { it.key == adapter.key }) platformAdapters.add(adapter)
        }
    }

    suspend fun eachPlatform(block: suspend (platformAdapter: AnyPlatformAdapter) -> Unit) {
        for (platform in platformAdapters) {
            block(platform)
        }
    }

    suspend fun init() {
        eachPlatform {
            it.initialize(this)
        }

        plugins.initialize()
    }

    suspend fun start() {
        eachPlatform {
            it.start(this)
        }
    }

    suspend fun stop() {
        eachPlatform {
            it.stop()
        }

        plugins.unloadAll()
        tempFs.cleanup()
    }
}

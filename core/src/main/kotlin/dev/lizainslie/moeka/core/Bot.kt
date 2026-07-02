package dev.lizainslie.moeka.core

import dev.lizainslie.moeka.core.commands.Commands
import dev.lizainslie.moeka.core.config.Configs
import dev.lizainslie.moeka.core.data.DbContext
import dev.lizainslie.moeka.core.data.tables.DeveloperOptionsTable
import dev.lizainslie.moeka.core.data.tables.ModuleCommunitySettingsTable
import dev.lizainslie.moeka.core.data.tables.ModuleSwitchTable
import dev.lizainslie.moeka.core.data.tables.ModuleVersionTable
import dev.lizainslie.moeka.core.fs.BotFS
import dev.lizainslie.moeka.core.logging.Logging
import dev.lizainslie.moeka.core.manual.ManualRegistry
import dev.lizainslie.moeka.core.modules.AbstractModule
import dev.lizainslie.moeka.core.modules.ModuleRegistry
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter

class Bot(
    vararg val baseModules: AbstractModule = emptyArray(),
) {
    val modules = ModuleRegistry(this)
    val commands = Commands(this)
    val manPages = ManualRegistry()

    val platformAdapters = mutableListOf<AnyPlatformAdapter>()

    init {
        // generate the base folder structure if it doesn't exist
        BotFS.generateBaseStructure()

        // check to ensure that configs are valid
        Configs.checkConfigs()

        Logging.init()

        DbContext.connect()
        DbContext.tables += ModuleVersionTable
        DbContext.tables += ModuleSwitchTable
        DbContext.tables += DeveloperOptionsTable
        DbContext.tables += ModuleCommunitySettingsTable
        DbContext.migrate()
    }

    suspend fun loadModules() {
        baseModules.forEach {
            modules.loadBundledModule(it)
        }

        modules.loadJarModules()
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

        modules.initialize()
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

        modules.unloadAll()
        BotFS.Temp.cleanup()
    }
}

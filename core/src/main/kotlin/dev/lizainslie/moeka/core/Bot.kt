package dev.lizainslie.moeka.core

import dev.lizainslie.moeka.core.cache.Caches
import dev.lizainslie.moeka.core.cache.provider.CacheProvider
import dev.lizainslie.moeka.core.cache.provider.memory.InMemoryCacheProvider
import dev.lizainslie.moeka.core.commands.Commands
import dev.lizainslie.moeka.core.config.Configs
import dev.lizainslie.moeka.core.data.DbContext
import dev.lizainslie.moeka.core.data.tables.DeveloperOptionsTable
import dev.lizainslie.moeka.core.data.tables.ModuleSwitchTable
import dev.lizainslie.moeka.core.fs.BotFS
import dev.lizainslie.moeka.core.logging.Logging
import dev.lizainslie.moeka.core.manual.ManualRegistry
import dev.lizainslie.moeka.core.modules.AbstractModule
import dev.lizainslie.moeka.core.modules.ModuleRegistry
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Bot(
    vararg val baseModules: AbstractModule = emptyArray(),
) {
    private val log: Logger = LoggerFactory.getLogger(Bot::class.java)

    var cacheProvider: CacheProvider = InMemoryCacheProvider()
        private set

    val modules = ModuleRegistry(this)
    val commands = Commands(this)
    val caches by lazy {
        Caches(cacheProvider)
    }
    val manPages = ManualRegistry()

    val platformAdapters = mutableListOf<AnyPlatformAdapter>()

    init {
        // generate the base folder structure if it doesn't exist
        BotFS.generateBaseStructure()

        // check to ensure that configs are valid
        Configs.checkConfigs()

        Logging.init()

        DbContext.connect()
        DbContext.tables += ModuleSwitchTable
        DbContext.tables += DeveloperOptionsTable
        DbContext.migrate()
    }

    fun installCacheProvider(cacheProvider: CacheProvider) {
        this.cacheProvider = cacheProvider
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
        if (cacheProvider is InMemoryCacheProvider) {
            log.warn("Cache provider is not installed, defaulting to in-memory cache")
        }

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

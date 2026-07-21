package dev.lizainslie.moeka.core.plugins.registry

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.data.DbContext
import dev.lizainslie.moeka.core.data.entities.PluginVersion
import dev.lizainslie.moeka.core.fs.BotFs
import dev.lizainslie.moeka.core.logging.logPlugin
import dev.lizainslie.moeka.core.logging.suspendLogPlugin
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import dev.lizainslie.moeka.core.plugins.types.PluginManifest
import dev.lizainslie.moeka.core.plugins.PluginManifestService
import dev.lizainslie.moeka.core.plugins.PluginModuleProvider
import dev.lizainslie.moeka.core.plugins.PluginScopeArchetype
import dev.lizainslie.moeka.core.plugins.types.PluginSource
import dev.lizainslie.moeka.core.plugins.sortByDependencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.unloadKoinModules
import org.koin.core.parameter.parametersOf
import org.slf4j.LoggerFactory
import java.io.File

class PluginRegistryService : KoinComponent {
    private val bot: Bot by inject()
    private val botFs: BotFs by inject()

    private val manifests by inject<PluginManifestService>()

    val loadedPlugins = mutableListOf<LoadedPlugin>()
    private val log = LoggerFactory.getLogger(javaClass)

    fun load(plugin: LoadedPlugin) {
        logPlugin(plugin.instance) {
            if (loadedPlugins.any { it.name == plugin.name }) {
                log.warn("Plugin with name '${plugin.name}' is already loaded! Skipping.")
                return@logPlugin
            }

            val currentVersion = plugin.instance.manifest.version
            val versionFromDb = transaction { PluginVersion.getVersion(plugin.name) }

            fun updateVersionInDatabase() {
                transaction {
                    PluginVersion.upsert(plugin.name, currentVersion)
                }
            }

            if (versionFromDb != null) {
                if (currentVersion != versionFromDb) {
                    if (currentVersion > versionFromDb) {
                        log.info("Plugin '${plugin.name}' has been updated from version $versionFromDb to $currentVersion.")
                        // todo: migrate settings as necessary
                        updateVersionInDatabase()
                    }

                    updateVersionInDatabase()
                }
            }

            log.info("Running migrations for plugin '${plugin.name}'...")
            DbContext.migrate(plugin.instance.tables)

            loadedPlugins += plugin
            plugin.instance.onLoad()

            log.info("Loaded plugin '${plugin.name}'.")
        }
    }

    suspend fun loadExternalPlugin(file: File) {
        withContext(Dispatchers.IO) {
            log.info("Loading external plugin from ${file.absolutePath}...")

            if (!file.exists()) return@withContext // this should never hit, just like me with pretty women

            if (!file.isFile) {
                log.warn("Plugin path ${file.absolutePath} is not a file, skipping.")
                return@withContext
            }

            if (file.extension != "jar") {
                log.warn("Plugin file ${file.absolutePath} is not a JAR file, skipping.")
                return@withContext
            }

            val url = file.toURI().toURL()
            val cl = PluginClassLoader(url, Bot::class.java.classLoader)

            val stream =
                cl.getResourceAsStream("moeka.module.json")
                    ?: run {
                        log.warn("Plugin JAR ${file.absolutePath} is missing manifest file 'moeka.module.json', skipping.")
                        return@withContext
                    }

            val manifest =
                Json.decodeFromString<PluginManifest>(
                    stream.reader().readText(),
                )
            log.debug("Successfully read manifest. Module provider class: ${manifest.pluginModuleProviderClass}")

            manifests[manifest.name] = manifest

            val moduleProviderClass = cl.loadClass(manifest.pluginModuleProviderClass)
            val moduleProvider = moduleProviderClass.getDeclaredField("INSTANCE").get(null) as PluginModuleProvider

            val pluginClass: Class<out AbstractPlugin> = cl.loadPluginClass(manifest.pluginClass).getOrElse {
                log.warn("Failed to load plugin class: ${manifest.pluginModuleProviderClass}")
                return@withContext
            }

            val module = moduleProvider.createModule()
            val scope = getKoin().createScope(
                scopeId = "pluginScope_${manifest.name}",
                qualifier = PluginScopeArchetype
            )

            val pluginInstance = scope.get<AbstractPlugin>(pluginClass.kotlin) { parametersOf(scope) }

            log.debug("Plugin class loaded, name: '${manifest.name}'")

            load(
                LoadedPlugin(
                    name = manifest.name,
                    instance = pluginInstance,
                    module = module,
                    source = PluginSource.EXTERNAL,
                    classLoader = cl,
                    scope = scope,
                ),
            )
        }
    }

    fun get(name: String): LoadedPlugin? = loadedPlugins.find { it.name == name }

    suspend fun initialize() {
        log.info("Initializing ${loadedPlugins.size} plugins.")
        loadedPlugins.sortByDependencies()

        for (module in loadedPlugins) {
            initialize(module)
        }
    }

    suspend fun initialize(plugin: LoadedPlugin) {
        suspendLogPlugin(plugin.instance) {
            log.info("Initializing module '${plugin.name}'.")

            plugin.instance.onInit()

            bot.commands.registerPluginCommands(plugin.instance)
        }
    }

    suspend fun initialize(name: String) {
        val mod = get(name) ?: return
        initialize(mod)
    }

    suspend fun unload(name: String) {
        val mod = get(name) ?: return
        unload(mod)
    }

    suspend fun unload(plugin: LoadedPlugin) {
        suspendLogPlugin(plugin.instance) {
            log.info("Unloading module '${plugin.name}'.")

            plugin.instance.onUnload()

            bot.commands.unregisterPluginCommands(plugin.instance)

            plugin.scope.close()
            unloadKoinModules(plugin.module)

            loadedPlugins.remove(plugin)
        }
    }

    suspend fun unloadAll() {
        log.info("Unloading all modules.")
        for (mod in loadedPlugins) {
            unload(mod)
        }
    }

    suspend fun unloadExternal() {
        log.info("Unloading all external modules.")
        for (mod in loadedPlugins.filter { it.source == PluginSource.EXTERNAL }) {
            unload(mod)
        }
    }

    suspend fun reload(name: String) {
        val mod = get(name) ?: return
        reload(mod)
    }

    /**
     * Reload `mod` from disk
     */
    suspend fun reload(plugin: LoadedPlugin) {
        suspendLogPlugin(plugin.instance) {
            log.info("Reloading plugin '${plugin.name}'.")
            val name = plugin.name
            if (plugin.source == PluginSource.INTERNAL) {
                log.warn("Internal plugin '$name' cannot be reloaded.")
                return@suspendLogPlugin
            }

            unload(plugin)

            val jar =
                botFs.pluginsDir.resolve("${plugin.name}.jar").also {
                    if (!it.exists()) {
                        run {
                            log.error("Plugin '$name' is missing its JAR file, cannot reload.")
                            return@suspendLogPlugin
                        }
                    }
                }

            loadExternalPlugin(jar)

            log.debug("Calling garbage collector to unload old plugin classes.")
            System.gc() // encourages class unloading

            initialize(name)
            log.info("Reloaded plugin '${plugin.name}'.")
        }
    }

    suspend fun fullReload() {
        log.info("Reloading all plugins")
        for (mod in loadedPlugins.filter { it.source == PluginSource.EXTERNAL }) {
            reload(mod)
        }
    }

    suspend fun loadJarPlugins(dir: File = botFs.pluginsDir) {
        log.info("Loading JAR plugins from ${dir.absolutePath}")
        withContext(Dispatchers.IO) {
            dir.listFiles().filter { it.isFile && it.extension == "jar" }.forEach {
                loadExternalPlugin(it)
            }
        }
    }
}
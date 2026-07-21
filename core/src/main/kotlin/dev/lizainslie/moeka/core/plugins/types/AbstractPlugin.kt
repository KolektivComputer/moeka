package dev.lizainslie.moeka.core.plugins.types

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.commands.RootCommand
import dev.lizainslie.moeka.core.config.ConfigBase
import dev.lizainslie.moeka.core.config.ConfigService
import dev.lizainslie.moeka.core.data.entities.ModuleSwitch
import dev.lizainslie.moeka.core.fs.PluginTemp
import dev.lizainslie.moeka.core.fs.PluginTempContext
import dev.lizainslie.moeka.core.manual.Manual
import dev.lizainslie.moeka.core.manual.ManualProvider
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.PlatformKey
import dev.lizainslie.moeka.core.platforms.SupportPlatforms
import dev.lizainslie.moeka.core.plugins.PluginScopeArchetype
import dev.lizainslie.moeka.core.plugins.settings.PluginCommunitySettingsMap
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinitionDsl
import dev.lizainslie.moeka.core.plugins.settings.schema.defineSettings
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.core.scope.get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractPlugin(
    val koinScope: Scope,
    val optional: Boolean = true,
    val visibility: PluginVisibility = PluginVisibility.PUBLIC,
    val description: String = "No description provided",
    val commands: Set<RootCommand> = emptySet(),
    val tables: Set<Table> = emptySet(),
    val dependencies: Set<String> = emptySet(),
) : ManualProvider, KoinComponent {
    protected val bot by inject<Bot>()
    protected val configService by inject<ConfigService>()

    val manifest = koinScope.get<PluginManifest>()
    val name = manifest.name

    val temp by lazy { koinScope.get<PluginTemp>() }

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    protected var communitySettingsDefinitions: List<SettingDefinition<*>>? = null

    val communitySettings by lazy {
        PluginCommunitySettingsMap(name, communitySettingsDefinitions ?: emptyList())
    }

    fun defineCommunitySettings(block: SettingDefinitionDsl.() -> Unit) {
        communitySettingsDefinitions = defineSettings(block)
    }

    open fun onLoad() {}

    open fun onUnload() {
        koinScope.close()

        temp.cleanup()
    }

    open fun onInit() {}

    protected inline fun <reified TConfig : ConfigBase> config() = configService.pluginConfig<TConfig>(this.name)

    open fun isEnabledForCommunity(communityId: PlatformId) =
        transaction {
            supportsPlatform(communityId.platform) && ModuleSwitch.isModuleEnabled(communityId, name)
        }

    fun supportsPlatform(platform: AnyPlatformAdapter) =
        this::class.annotations.filterIsInstance<SupportPlatforms>().any {
            it.platforms.contains(platform::class)
        }

    fun supportsPlatform(platform: PlatformKey) =
        bot.platformAdapters
            .firstOrNull { it.key == platform }
            ?.let { supportsPlatform(it) } ?: false

    fun <T : Any> withTempContext(block: PluginTempContext.() -> T): T {
        val context = temp.createContext()
        val result = context.block()
        temp.removeContext(context)
        return result
    }

    suspend fun <T : Any> withTempContextSuspend(block: suspend PluginTempContext.() -> T): T {
        val context = temp.createContext()
        val result = context.block()
        temp.removeContext(context)
        return result
    }

    override fun registerManPage(man: Manual) {
        bot.manPages.registerPluginManPage(this, man)
    }
}
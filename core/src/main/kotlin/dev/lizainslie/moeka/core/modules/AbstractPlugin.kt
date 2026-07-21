package dev.lizainslie.moeka.core.modules

import dev.lizainslie.moeka.core.Bot
import dev.lizainslie.moeka.core.commands.RootCommand
import dev.lizainslie.moeka.core.config.ConfigBase
import dev.lizainslie.moeka.core.config.Configs
import dev.lizainslie.moeka.core.data.entities.ModuleSwitch
import dev.lizainslie.moeka.core.fs.BotFS
import dev.lizainslie.moeka.core.fs.ModuleTemp
import dev.lizainslie.moeka.core.fs.ModuleTempContext
import dev.lizainslie.moeka.core.manual.Manual
import dev.lizainslie.moeka.core.manual.ManualProvider
import dev.lizainslie.moeka.core.modules.settings.PluginCommunitySettingsMap
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinitionDsl
import dev.lizainslie.moeka.core.modules.settings.schema.defineSettings
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.PlatformKey
import dev.lizainslie.moeka.core.platforms.SupportPlatforms
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractPlugin(
    val name: String,
    val optional: Boolean = true,
    val visibility: PluginVisibility = PluginVisibility.PUBLIC,
    val description: String = "No description provided",
    val commands: Set<RootCommand> = emptySet(),
    val tables: Set<Table> = emptySet(),
    val dependencies: Set<String> = emptySet(),
) : ManualProvider {
    val temp =
        ModuleTemp(
            BotFS.Temp.plugins.resolve(name),
        )
    protected lateinit var bot: Bot
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    protected var communitySettingsDefinitions: List<SettingDefinition<*>>? = null

    val communitySettings by lazy {
        PluginCommunitySettingsMap(name, communitySettingsDefinitions ?: emptyList())
    }

    fun defineCommunitySettings(block: SettingDefinitionDsl.() -> Unit) {
        communitySettingsDefinitions = defineSettings(block)
    }

    lateinit var manifest: PluginManifest
        private set

    fun loadManifest(moduleManifest: PluginManifest) {
        manifest = moduleManifest
    }

    open fun onLoad() {}

    open fun onUnload() {
        temp.cleanup()
    }

    open fun onInit(bot: Bot) {
        this.bot = bot
    }

    inline fun <reified TConfig : ConfigBase> config() = Configs.moduleConfig<TConfig>(this.name)

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

    fun <T : Any> withTempContext(block: ModuleTempContext.() -> T): T {
        val context = temp.createContext()
        val result = context.block()
        temp.removeContext(context)
        return result
    }

    suspend fun <T : Any> withTempContextSuspend(block: suspend ModuleTempContext.() -> T): T {
        val context = temp.createContext()
        val result = context.block()
        temp.removeContext(context)
        return result
    }

    override fun registerManPage(man: Manual) {
        bot.manPages.registerModuleManPage(this, man)
    }
}

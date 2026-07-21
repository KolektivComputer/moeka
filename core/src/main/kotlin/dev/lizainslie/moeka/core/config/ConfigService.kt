package dev.lizainslie.moeka.core.config

import org.koin.core.component.KoinComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class ConfigService : KoinComponent {
    val loadedConfigs = mutableListOf<Config<*>>()

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun <TConfig : ConfigBase> getConfigKey(klass: KClass<out TConfig>) = ConfigFile.get(klass).key

    inline fun <reified TConfig : ConfigBase> getConfigKey() = getConfigKey(TConfig::class)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified TConfig : ConfigBase> config(): Config<TConfig> {
        val key = getConfigKey<TConfig>()
        if (loadedConfigs.any { it.key == key }) {
            return loadedConfigs.first { it.key == key } as Config<TConfig>
        } else {
            log.debug("Registering new config of type ${TConfig::class.simpleName} with key '$key'")
            val config = Config<TConfig>(key)
            loadedConfigs += config
            return config
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified TConfig : ConfigBase> pluginConfig(pluginName: String): Config<TConfig> {
        val key = getConfigKey<TConfig>()
        if (loadedConfigs.any { it.key == key }) {
            return loadedConfigs.first { it.key == key } as Config<TConfig>
        } else {
            log.debug("Registering new plugin config of type ${TConfig::class.simpleName} with key '$key' for plugin '$pluginName'")
            val config = Config<TConfig>(key, pluginName)
            loadedConfigs += config
            return config
        }
    }

//    fun getBotConfigFile() = BotFS.configDir.resolve("config.json")
//
//    fun getPlatformConfigFile(key: PlatformKey) = BotFS.platformConfigDir.resolve("${key.key}.json")

//    fun <TConfig : ConfigBase> loadPlatformConfig(key: PlatformKey, serializer: KSerializer<TConfig>, createDefault: () -> TConfig): TConfig {
//        val configFile = getPlatformConfigFile(key)
//
//        if (!configFile.exists()) {
//            // create & write default
//            configFile.createNewFile()
//            configFile.writeText(json.encodeToString(serializer, createDefault()))
//
//            // then warn & exit
//            println("Platform config file ${configFile.absolutePath} does not exist, but platform ${key.key} is enabled. A new default config has been generated. Please fill it out and try again.")
//            exitProcess(1)
//        }
//
//        return json.decodeFromString(serializer, configFile.readText())
//    }

    fun checkConfigs() {
        // todo: create new check logic
//        var shouldExit = false
//        val botConfig = getBotConfigFile()
//
//        if (!botConfig.exists()) {
//            shouldExit = true
//
//            // generate a default
//            botConfig.createNewFile()
//            botConfig.writeText(
//                json.encodeToString( BotConfig()),
//                Charsets.UTF_8
//            )
//
//            // print a warning
//            println(
//                """Main config was not present at ${botConfig.absolutePath}.
//                    |A new default config has been generated. Please fill it out before continuing.""".trimMargin())
//        }
//
//        if (shouldExit) exitProcess(1)
    }
}

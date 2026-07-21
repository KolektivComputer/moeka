package dev.lizainslie.moeka.core.config

import dev.lizainslie.moeka.core.fs.BotFs
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Config<TConfig : ConfigBase>(
    val key: String,
    val serializer: KSerializer<TConfig>,
    klass: KClass<out TConfig>,
    val pluginName: String? = null,
) : KoinComponent {
    private val botFs: BotFs by inject()

    lateinit var currentValue: TConfig
        private set

    val jsonValue by lazy {
        prettyPrintJson.encodeToString(serializer, this.currentValue)
    }

    private val prettyPrintJson =
        Json {
            prettyPrint = true
            encodeDefaults = true
        }

    private val json =
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            // todo: define some other defaults
        }

    private val log = LoggerFactory.getLogger(javaClass)

    private val annotation = ConfigFile.get(klass)

    private val file: File =
        when (annotation.type) {
            ConfigType.PLUGIN -> {
                if (pluginName == null) throw RuntimeException("Error loading module config ${klass.simpleName}: moduleName is null")
                botFs.pluginConfigDir.resolve(pluginName).resolve(annotation.name)
            }
            ConfigType.ROOT -> botFs.configDir.resolve(annotation.name)
            ConfigType.PLATFORM -> botFs.platformConfigDir.resolve(annotation.name)
        }

    init {
        load()
    }

    fun load() {
        log.info("${if (::currentValue.isInitialized) "Rel" else "L"}oading config with key '$key' from $file")
        val contents = file.readText(Charsets.UTF_8)
        val configValue = json.decodeFromString(serializer, contents)

        // todo: make this error better and  refactor validation system to
        //          return better errors
        if (!configValue.validate()) {
            throw RuntimeException("Invalid config value in $file:\n $contents")
        }

        currentValue = configValue

        log.debug("Calling onLoad hook for config with key '$key'")
        currentValue.onLoad()
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): TConfig = currentValue
}

inline fun <reified TConfig : ConfigBase> Config(
    key: String,
    moduleName: String? = null,
) = Config(
    key = key,
    serializer = Json.serializersModule.serializer<TConfig>(),
    klass = TConfig::class,
    pluginName = moduleName,
)

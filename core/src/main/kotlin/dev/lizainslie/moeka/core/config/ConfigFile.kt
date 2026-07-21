package dev.lizainslie.moeka.core.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ConfigFile(
    val name: String,
    val key: String,
    val type: ConfigType,
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(ConfigService::class.java)

        fun <TConfig : ConfigBase> get(klass: KClass<out TConfig>): ConfigFile {
            val annotations = klass.annotations.filterIsInstance<ConfigFile>()
            if (annotations.size != 1) {
                // todo: error.
                throw RuntimeException("${klass.simpleName} must have exactly one ConfigFile annotation!")
            }

            val configFile = annotations.first()
            log.debug(
                "Found config file annotation: name='{}', key='{}', type='{}' for config class '{}'",
                configFile.name,
                configFile.key,
                configFile.type,
                klass.simpleName,
            )

            return configFile
        }
    }
}

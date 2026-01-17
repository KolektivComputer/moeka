package dev.lizainslie.moeka.core.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ConfigFile(
    val name: String,
    val key: String,
    val type: ConfigType,
)

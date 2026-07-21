package dev.lizainslie.moeka.core.plugins.settings.schema

const val SETTING_KEY_MAX_LENGTH = 255

data class SettingDefinition<TSettingValue : Any>(
    val key: String,
    val type: SettingType<TSettingValue>,
    val displayName: String,
    val description: String,
    val optional: Boolean,
    val defaultValue: TSettingValue? = null,
)
package dev.lizainslie.moeka.core.modules.settings.holder

import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition

abstract class AbstractSettingsHolder(
    val definitions: List<SettingDefinition<*>>
) {
    constructor(vararg definitions: SettingDefinition<*>) : this(definitions.toList())

    inline fun <reified TSettingValue : Any> getDefinition(key: String): SettingDefinition<TSettingValue> {
        val definition = definitions.find { it.key == key }
            ?: throw IllegalArgumentException("No setting definition found for key '$key'.")

        if (definition.type.type != TSettingValue::class)
            throw IllegalArgumentException("Setting definition for key '$key' has type '${definition.type::class.simpleName}', expected '${TSettingValue::class.simpleName}'.")

        @Suppress("UNCHECKED_CAST")
        return definition as SettingDefinition<TSettingValue>
    }
}
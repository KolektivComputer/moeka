@file:OptIn(ExperimentalContracts::class)

package dev.lizainslie.moeka.core.modules.settings.schema

import java.util.EnumSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.time.Duration

fun defineSettings(block: SettingDefinitionDsl.() -> Unit): List<SettingDefinition<*>> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val dsl = SettingDefinitionDsl()
    dsl.block()
    return dsl.settings
}

class SettingDefinitionDsl {
    val settings = mutableListOf<SettingDefinition<*>>()

    fun <TSettingValue : Any> setting(
        key: String,
        type: SettingType<TSettingValue>,
        displayName: String,
        description: String,
        optional: Boolean = true,
        defaultValue: TSettingValue? = null,
    ) {
        if (key.length > SETTING_KEY_MAX_LENGTH) {
            throw IllegalArgumentException("Setting key '$key' exceeds maximum length of $SETTING_KEY_MAX_LENGTH characters.")
        }

//        if (!optional && defaultValue == null) {
//            throw IllegalArgumentException("Default value must be non-null if setting is not optional.")
//        }

        settings.add(
            SettingDefinition(
                key = key,
                type = type,
                displayName = displayName,
                description = description,
                optional = optional,
                defaultValue = defaultValue
            )
        )
    }

    fun string(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: String? = null,
    ) = setting(key, SettingType.StringSettingType, displayName, description, optional, defaultValue)

    fun bool(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Boolean? = null,
    ) = setting(key, SettingType.BooleanSettingType, displayName, description, optional, defaultValue)

    fun int(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Int? = null,
    ) = setting(key, SettingType.IntegerSettingType, displayName, description, optional, defaultValue)

    fun uint(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: UInt? = null,
    ) = setting(key, SettingType.UnsignedIntegerSettingType, displayName, description, optional, defaultValue)

    fun long(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Long? = null
    ) = setting(key, SettingType.LongSettingType, displayName, description, optional, defaultValue)

    fun ulong(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: ULong? = null
    ) = setting(key, SettingType.UnsignedLongSettingType, displayName, description, optional, defaultValue)

    fun float(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Float? = null,
    ) = setting(key, SettingType.FloatSettingType, displayName, description, optional, defaultValue)

    fun double(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Double? = null,
    ) = setting(key, SettingType.DoubleSettingType, displayName, description, optional, defaultValue)

    fun duration(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: Duration? = null,
    ) = setting(key, SettingType.DurationSettingType, displayName, description, optional, defaultValue)

    fun <T : Enum<T>> enum(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: T? = null,
        enumClass: KClass<T>,
        enumConstants: EnumSet<T>
    ) = setting(
        key = key,
        type = SettingType.EnumSettingType(enumClass, enumConstants),
        displayName = displayName,
        description = description,
        optional = optional,
        defaultValue = defaultValue
    )

    inline fun <reified T : Enum<T>> enum(
        key: String,
        displayName: String,
        description: String,
        optional: Boolean = false,
        defaultValue: T? = null,
    ) = enum(
        key = key,
        displayName = displayName,
        description = description,
        optional = optional,
        defaultValue = defaultValue,
        enumClass = T::class,
        enumConstants = EnumSet.allOf(T::class.java)
    )
}
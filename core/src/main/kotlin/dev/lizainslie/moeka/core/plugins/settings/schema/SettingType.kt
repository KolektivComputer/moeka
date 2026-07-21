package dev.lizainslie.moeka.core.plugins.settings.schema

import dev.lizainslie.moeka.core.data.serializers.ColorAsHexSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.Color
import java.util.EnumSet
import kotlin.reflect.KClass
import kotlin.time.Duration

sealed interface SettingType<TSettingValue : Any> {
    val type: KClass<TSettingValue>

    fun serialize(value: TSettingValue?): String
    fun deserialize(value: String): TSettingValue?

    open class ListSetting<TSettingValue : Any>(
        val itemType: SettingType<TSettingValue>,
    ) : SettingType<List<TSettingValue?>> {
        @Suppress("UNCHECKED_CAST")
        override val type = List::class as KClass<List<TSettingValue?>>

        override fun serialize(value: List<TSettingValue?>?) =
            Json.encodeToString(value?.map { itemType.serialize(it) })
        override fun deserialize(value: String): List<TSettingValue?>? =
            Json.decodeFromString<List<String?>?>(value)?.map {
                if (it != null) itemType.deserialize(it)
                else null
            }

        object StringList : ListSetting<String>(StringSettingType)
        object BooleanList : ListSetting<Boolean>(BooleanSettingType)
        object IntegerList : ListSetting<Int>(IntegerSettingType)
        object UnsignedIntegerList : ListSetting<UInt>(UnsignedIntegerSettingType)
        object LongList : ListSetting<Long>(LongSettingType)
        object UnsignedLongList : ListSetting<ULong>(UnsignedLongSettingType)
        object FloatList : ListSetting<Float>(FloatSettingType)
        object DoubleList : ListSetting<Double>(DoubleSettingType)
        object DurationList : ListSetting<Duration>(DurationSettingType)
        object ColorList : ListSetting<Color>(ColorSettingType)
    }

    object StringSettingType : SettingType<String> {
        override val type = String::class
        override fun serialize(value: String?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<String?>(value)
    }

    object BooleanSettingType : SettingType<Boolean> {
        override val type = Boolean::class
        override fun serialize(value: Boolean?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Boolean?>(value)
    }

    object IntegerSettingType : SettingType<Int> {
        override val type = Int::class
        override fun serialize(value: Int?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Int?>(value)
    }

    object UnsignedIntegerSettingType : SettingType<UInt> {
        override val type = UInt::class
        override fun serialize(value: UInt?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<UInt?>(value)
    }

    object LongSettingType : SettingType<Long> {
        override val type = Long::class
        override fun serialize(value: Long?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Long?>(value)
    }

    object UnsignedLongSettingType : SettingType<ULong> {
        override val type = ULong::class
        override fun serialize(value: ULong?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<ULong?>(value)
    }

    object FloatSettingType : SettingType<Float> {
        override val type = Float::class
        override fun serialize(value: Float?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Float?>(value)
    }

    object DoubleSettingType : SettingType<Double> {
        override val type = Double::class
        override fun serialize(value: Double?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Double?>(value)
    }

    object DurationSettingType : SettingType<Duration> {
        override val type = Duration::class
        override fun serialize(value: Duration?) = Json.encodeToString(value)
        override fun deserialize(value: String) = Json.decodeFromString<Duration?>(value)
    }

    class EnumSettingType<T : Enum<T>>(enumClass: KClass<T>, private val enumConstants: EnumSet<T>) : SettingType<T> {
        override val type = enumClass
        override fun serialize(value: T?) = Json.encodeToString(value?.ordinal)
        override fun deserialize(value: String): T? = Json.decodeFromString<Int?>(value)?.let { o -> enumConstants.first { e -> e.ordinal == o } }
    }

    object ColorSettingType : SettingType<Color> {
        override val type = Color::class
        override fun serialize(value: Color?) = Json.encodeToString(ColorAsHexSerializer.nullable, value)
        override fun deserialize(value: String) = Json.decodeFromString(ColorAsHexSerializer.nullable, value)
    }
}
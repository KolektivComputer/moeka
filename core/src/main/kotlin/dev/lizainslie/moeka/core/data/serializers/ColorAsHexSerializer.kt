package dev.lizainslie.moeka.core.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.Color

object ColorAsHexSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ColorAsHex", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        val hex = value.rgb.and(0xFFFFFF).toString(16).padStart(6, '0').uppercase()
        encoder.encodeString(hex)
    }

    override fun deserialize(decoder: Decoder): Color {
        val hex = decoder.decodeString()
        return Color(hex.toInt(16))
    }
}

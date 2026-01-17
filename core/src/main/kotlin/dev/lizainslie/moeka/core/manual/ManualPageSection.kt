package dev.lizainslie.moeka.core.manual

import kotlinx.serialization.Serializable

@Serializable
data class ManualPageSection(
    val heading: String,
    val content: String,
)

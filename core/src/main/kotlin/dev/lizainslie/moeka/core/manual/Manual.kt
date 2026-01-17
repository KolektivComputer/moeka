package dev.lizainslie.moeka.core.manual

import kotlinx.serialization.Serializable

@Serializable
data class Manual(
    val identifier: String,
    val title: String,
    val pages: List<ManualPage>,
)

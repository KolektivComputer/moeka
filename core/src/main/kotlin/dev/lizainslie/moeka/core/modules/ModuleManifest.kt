package dev.lizainslie.moeka.core.modules

import kotlinx.serialization.Serializable

@Serializable
data class ModuleManifest(
    val mainClass: String,
)

package dev.lizainslie.moeka.core.modules

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
data class ModuleManifest(
    val mainClass: String,
    val version: Version,
)

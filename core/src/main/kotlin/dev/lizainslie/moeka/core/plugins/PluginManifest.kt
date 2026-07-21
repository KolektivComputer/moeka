package dev.lizainslie.moeka.core.plugins

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
data class PluginManifest(
    val mainClass: String,
    val version: Version,
)

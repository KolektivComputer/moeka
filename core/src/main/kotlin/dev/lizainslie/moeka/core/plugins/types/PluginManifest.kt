package dev.lizainslie.moeka.core.plugins.types

import io.github.z4kn4fein.semver.Version
import kotlinx.serialization.Serializable

@Serializable
data class PluginManifest(
    val name: String,
    val pluginClass: String,
    val pluginModuleProviderClass: String,
    val version: Version,
)

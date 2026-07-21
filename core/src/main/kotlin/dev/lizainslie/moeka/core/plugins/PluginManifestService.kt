package dev.lizainslie.moeka.core.plugins

import dev.lizainslie.moeka.core.plugins.types.PluginManifest

class PluginManifestService {
    val manifests = mutableMapOf<String, PluginManifest>()

    operator fun get(pluginName: String) = manifests[pluginName]
    operator fun set(pluginName: String, plugin: PluginManifest) {
        manifests[pluginName] = plugin
    }
}